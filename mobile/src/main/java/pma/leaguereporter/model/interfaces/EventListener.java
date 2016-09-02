package pma.leaguereporter.model.interfaces;

import pma.leaguereporter.model.objects.EventData;

public interface EventListener {
	void onEvent(EventData event);
}
