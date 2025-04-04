package pro.azhidkov.platform.kotlin


fun <T, S> T.ifNotNull(subj: S?, block: T.(S) -> T): T {
    if (subj == null) {
        return this
    }
    return block(subj)
}