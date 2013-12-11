arel.sceneReady(function()
{
	console.log("sceneReady");
	
    arel.Scene.getLocation(function(location){initScene(location);});

	//set a listener to tracking to get information about when the image is tracked
	arel.Events.setListener(arel.Scene, function(type, param){locationUpdateHandler(type, param);});
                

});

function locationUpdateHandler(type, param)
{
    
	//check if there is tracking information available
	if(param !== undefined && arel.Scene.getObject("1") && arel.Scene.getObject("2") && arel.Scene.getObject("3"))
	{
		// location update
		if(type && type == arel.Events.Scene.ONLOCATIONUPDATE)
		{
        
			var offset = 0.0002;
            var south = new arel.LLA(param.getLatitude()-offset, param.getLongitude(), param.getAltitude(), param.getAccuracy());
            var north = new arel.LLA(param.getLatitude()+offset, param.getLongitude(), param.getAltitude(), param.getAccuracy());
            var west = new arel.LLA(param.getLatitude(), param.getLongitude()-offset, param.getAltitude(), param.getAccuracy());
            
            arel.Scene.getObject("1").setLocation(west);
            arel.Scene.getObject("2").setLocation(north);
            arel.Scene.getObject("3").setLocation(south);

		}

	}
};

function initScene(location)
{
    var offset = 0.0002;
    var south = new arel.LLA(location.getLatitude()-offset, location.getLongitude(), location.getAltitude(), location.getAccuracy());
    var north = new arel.LLA(location.getLatitude()+offset, location.getLongitude(), location.getAltitude(), location.getAccuracy());
    var west = new arel.LLA(location.getLatitude(), location.getLongitude()-offset, location.getAltitude(), location.getAccuracy());
    
    //create new POI/billboard
    var northBillboard = new arel.Object.POI();
    northBillboard.setID("2");
    northBillboard.setTitle("North");
    northBillboard.setLocation(north);
    northBillboard.setThumbnail("");
    northBillboard.setIcon("");
    northBillboard.setVisibility(true,false,false);
    
    arel.Scene.addObject(northBillboard);
    
    
    var southBillboard = new arel.Object.POI();
    southBillboard.setID("3");
    southBillboard.setTitle("South");
    southBillboard.setLocation(south);
    southBillboard.setThumbnail("");
    southBillboard.setIcon("");
    southBillboard.setVisibility(true,false,false);
    arel.Scene.addObject(southBillboard);
    
    arel.Scene.getObject("1").setLocation(west);
    arel.Scene.getObject("1").setVisibility(true,false,true);

};