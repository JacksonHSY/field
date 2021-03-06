package com.yuminsoft.ams.system.editor;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wulinjie on 2017/6/17.
 */
public class DateEditor extends PropertyEditorSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateEditor.class);

    private boolean emptyAsNull;
    private String defaultFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String[] DATE_PATTERNS =
            {"yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss"};

    public DateEditor(boolean emptyAsNull) {
        this.emptyAsNull = emptyAsNull;
    }

    public DateEditor(boolean emptyAsNull, String dateFormat) {
        this.emptyAsNull = emptyAsNull;
        defaultFormat = dateFormat;
    }

    @Override
    public String getAsText() {
        Date localDate = (Date) getValue();
        return localDate != null ? new SimpleDateFormat(defaultFormat).format(localDate) : "";
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null) {
            setValue(null);
        } else {
            String str = text.trim();
            if (emptyAsNull && "".equals(str)) {
                setValue(null);
            } else {
                try {
                    setValue(DateUtils.parseDate(str, DATE_PATTERNS));
                } catch (ParseException e) {
                    LOGGER.warn(e.getMessage());

                    setValue(null);
                }
            }
        }
    }
}
