package project.shop.web;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Component
public class NumberFormatter implements Formatter<Number> {
  @Override
  public String print(Number object, Locale locale) {
    return NumberFormat.getIntegerInstance(locale).format(object);
  }

  @Override
  public Number parse(String text, Locale locale) throws ParseException {
    return NumberFormat.getIntegerInstance(locale).parse(text);
  }

}
