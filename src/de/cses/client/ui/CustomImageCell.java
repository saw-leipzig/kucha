package de.cses.client.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;

import de.cses.client.depictions.ImageXTemplate;

public class CustomImageCell extends AbstractCell<String> {
    private ImageXTemplate imageTemplate = GWT.create(ImageXTemplate.class);

    @Override
    public void render(Context context, String valueIsUrl, SafeHtmlBuilder sb) {
      if (valueIsUrl != null) {
        SafeUri url = UriUtils.fromString(valueIsUrl);
        sb.append(imageTemplate.createImage(url.asString()));
      }
    }
  }
