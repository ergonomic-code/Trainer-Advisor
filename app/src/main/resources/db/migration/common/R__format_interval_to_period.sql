CREATE OR REPLACE FUNCTION format_to_iso_8601(i INTERVAL)
    RETURNS TEXT
AS
$$
BEGIN
    SET LOCAL intervalstyle = 'iso_8601';
    RETURN i::TEXT;
END;
$$ LANGUAGE plpgsql;