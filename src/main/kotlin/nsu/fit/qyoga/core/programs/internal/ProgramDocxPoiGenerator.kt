package nsu.fit.qyoga.core.programs.internal

import nsu.fit.qyoga.core.exercises.api.model.Image
import nsu.fit.qyoga.core.programs.api.dtos.ProgramWithExercisesDto
import org.apache.poi.util.Units
import org.apache.poi.xwpf.usermodel.Document
import org.apache.poi.xwpf.usermodel.TableWidthType
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar.Factory
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr
import java.io.*
import java.math.BigInteger
import javax.imageio.ImageIO
import kotlin.math.min

fun generateProgramPoi(program: ProgramWithExercisesDto, fetchImages: (List<Long>) -> List<Image?>): InputStream {
    val buff = ByteArrayOutputStream()
    XWPFDocument().use { it ->
        it.document.body.sectPr = CTSectPr.Factory.newInstance().apply {
            pgMar = Factory.newInstance().apply {
                left = BigInteger("800")
                right = BigInteger("800")
            }
        }
        with(it.createParagraph()) {
            with(createRun()) {
                setText(program.title)
                fontSize = 24
                addBreak()
            }
        }

        program.exercises
            .forEach { ex ->
                with(it.createParagraph()) {
                    addRun(createRun().apply { this.setText(ex.title); isBold = true; fontSize = 14; })
                }

                with(it.createParagraph()) {
                    addRun(createRun().apply { addBreak(); this.setText(ex.description); addBreak() })
                }

                val images = fetchImages(ex.exerciseSteps.map { it.imageId ?: 0 }).associateBy { it?.id }
                ex.exerciseSteps
                    .forEach { step ->
                        with(it.createTable()) {
                            this.widthType = TableWidthType.PCT
                            this.removeBorders()
                            val cell = this.rows.last().getCell(0)
                            cell.widthType = TableWidthType.PCT
                            cell.setWidth("50%")
                            if (cell.paragraphs.size > 1) {
                                createRow()
                            }
                            with(cell.addParagraph()) {
                                val img = images[step.imageId]
                                if (img != null) {
                                    addRun(createRun().apply {
                                        setCellMargins(250, 250, 250, 250)
                                        val image = ImageIO.read(ByteArrayInputStream(img.data))
                                        val scale = min(1.0, 200.0 / image.width)
                                        addPicture(
                                            ByteArrayInputStream(img.data),
                                            XWPFDocument.PICTURE_TYPE_PNG,
                                            img.name,
                                            Units.toEMU(image.width * scale),
                                            Units.toEMU(image.height * scale)
                                        )
                                        val drawing = ctr.getDrawingArray(0)
                                        val anchor = getAnchorWithGraphic(
                                            drawing.getInlineArray(0).graphic,
                                            img.name,
                                            Units.toEMU(image.width * scale),
                                            Units.toEMU(image.height * scale),
                                            Units.toEMU(0.0),
                                            Units.toEMU(0.0),
                                            Units.toEMU(14.0),
                                            Units.toEMU(14.0),
                                        )
                                        drawing.anchorArray = arrayOf(anchor)
                                        drawing.removeInline(0)
                                    })
                                }
                            }
                            val cell2 = rows.last().addNewTableCell()
                            cell2.widthType = TableWidthType.PCT
                            cell2.setWidth("50%")
                            with(cell2.addParagraph()) {
                                addRun(createRun().apply {
                                    this.setText(step.description)
                                })
                            }
                        }
                        it.createParagraph()
                    }
            }
        it.write(buff)
    }
    return buff.toByteArray().inputStream()
}

@Throws(Exception::class)
private fun getAnchorWithGraphic(
    graphicalObject: CTGraphicalObject,
    drawingDescr: String,
    width: Int, height: Int,
    left: Int, top: Int,
    marginRight: Int, marginBottom: Int
): CTAnchor {
    val anchorXML =
        """<wp:anchor xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing" simplePos="0"
           relativeHeight="0" behindDoc="1" locked="0" layoutInCell="1" allowOverlap="1" distR="$marginRight" distB="$marginBottom">
              <wp:simplePos x="0" y="0"/>
              <wp:positionH relativeFrom="column">
                  <wp:posOffset>$left</wp:posOffset>
              </wp:positionH>
              <wp:positionV relativeFrom="paragraph">
                  <wp:posOffset>$top</wp:posOffset>
              </wp:positionV>
              <wp:extent cx="$width" cy="$height"/>
              <wp:effectExtent l="1000" t="0" r="0" b="0"/>
              <wp:wrapTight wrapText="bothSides">
                  <wp:wrapPolygon edited="0">
                      <wp:start x="0" y="0"/>
                      <wp:lineTo x="0" y="21600"/>
                      <wp:lineTo x="21600" y="21600"/>
                      <wp:lineTo x="21600" y="0"/>
                      <wp:lineTo x="0" y="0"/>
                  </wp:wrapPolygon>
              </wp:wrapTight>
              <wp:docPr id="1" name="Drawing 0" descr="$drawingDescr"/>
              <wp:cNvGraphicFramePr/>
           </wp:anchor>"""
    val drawing = CTDrawing.Factory.parse(anchorXML)
    val anchor: CTAnchor = drawing.getAnchorArray(0)
    anchor.graphic = graphicalObject
    return anchor
}

fun Image.typeOrNull() =
    when (this.mediaType) {
        "image/png" -> Document.PICTURE_TYPE_PNG
        "image/jpeg" -> Document.PICTURE_TYPE_JPEG
        else -> null
    }

fun Image.dimensions(): Pair<Int, Int> = with(ImageIO.read(ByteArrayInputStream(this.data))) {
    width to height
}
