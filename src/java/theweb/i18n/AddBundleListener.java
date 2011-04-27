package theweb.i18n;

import theweb.resources.ResourceListener;
import theweb.resources.ResourceLocation;

public class AddBundleListener implements ResourceListener {

	public void resourceFound(ResourceLocation resourceLocation) {
		if (!resourceLocation.getName().endsWith(".properties"))
			return;
		
        if (!resourceLocation.getName().contains("_"))
        	return;
        
        Resources.addBundle(resourceLocation);
	}
}
