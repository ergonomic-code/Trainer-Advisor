insert into images (name, media_type, size, data)
VALUES
    ('name1', 'test_media_type', 10, pg_read_file('../image/testImage.png')::bytea),
    ('name2', 'test_media_type', 9, pg_read_file('../image/testImage.png')::bytea)