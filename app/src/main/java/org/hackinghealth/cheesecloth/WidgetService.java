package org.hackinghealth.cheesecloth;

import android.content.Intent;
import android.widget.RemoteViewsService;

import org.hackinghealth.cheesecloth.widget.WidgetDataProvider;

/**
 * WidgetService is the {@link RemoteViewsService} that will return our RemoteViewsFactory
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }
}
