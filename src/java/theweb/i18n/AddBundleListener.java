package theweb.i18n;

public class AddBundleListener implements ResourceListener {

	public void bundleFound(ResourceLocation resourceLocation) {
		if (!resourceLocation.getName().endsWith(".properties"))
			return;
		
        if (!resourceLocation.getName().contains("_"))
        	return;
        
        Resources.addBundle(resourceLocation);
	}
}
