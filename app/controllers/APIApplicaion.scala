package controllers
import dispatch.json.Js
import models.APIResource
import models.Res
import models.ResList
import play.api.mvc.Action
import sjson.json.Serializer.SJSON
import util.APIRequestUtils
import util.StringUtil

object APIApplication extends AbstractController {
  

  
   def version_index() = Action {
     
	  Ok(views.html.index("version"))
  }
  
  
   def compare(ver1 : String, ver2 : String) = Action {
     Ok(views.html.compare_index(ver1, ver2))
   }
  def version_compare(ver1 : String, ver2 : String) = Action {
    var list1 = getAPIFunc("", ver1)
    var list2 = getAPIFunc("", ver2	)
    Ok(views.html.version_compare(list1, list2))
  }
  
  
  def getAPIFunc(keyword: String, version: String): List[APIResource]={
    var latestVersion = version
    if(StringUtil.isBlank(latestVersion)){
       latestVersion = versionTrackingService.getLastedVersion()
    }
    if(StringUtil.isNotBlank(keyword)){
      return apiResourceService.getAPIResourceByKeyword(keyword,latestVersion)
    }else{
      val apis = versionTrackingService.getPathListOfVersion(latestVersion)
      var list = List[APIResource]()
      apis.foreach(api => {
        val id = api;
        val resource = apiResourceService.getAPIResource(id, keyword, version)
        if (!resource.apis.isEmpty) {
          list ::= resource
        }
      })
      return list
    }
  }
    

  
  def getapi(keyword: String, version: String) = Action {
    var list = getAPIFunc(keyword, version)
//    val apis = versionTrackingService.getPathListOfVersion(latestVersion)
//    println("=====================")
//    println(apis)
//    println("=====================")
//    var list = List[APIResource]()
//    apis.foreach(api => {
//      val id = api;
//      val resource = apiResourceService.getAPIResource(id,keyword,version)
//      if(!resource.apis.isEmpty){
//        list ::= resource
//      }
//    })
    Ok(views.html.resources_list(list))
  }
  
  def getResources(start: String, size: String, rest: String,version:String) = Action {
    var iStart = 0
    var iSize = 10
    if (StringUtil.isNotBlank(start)) {
      iStart = start.toInt
    }
    if (StringUtil.isNotBlank(size)) {
      iSize = size.toInt
    }
    
    val list = apiResourceService.getAPIResources(iStart, iSize, rest,version)
    Ok(SJSON.toJSON(list))
  }

  def getListVersion(start: String, size: String) = Action {
    var iStart = 0
    var iSize = 10
    if (StringUtil.isNotBlank(start)) {
      iStart = start.toInt
    }
    if (StringUtil.isNotBlank(size)) {
      iSize = size.toInt
    }
    
    	
    val list = versionTrackingService.getListVersion(iStart,iSize)
    println("============VERSI")
    println(SJSON.toJSON(list))
    Ok(views.html.api_version_list(list))
  }
  
   def getResource(rest: String, id : String, keyword:String,version:String) = Action {
    val apiResource = apiResourceService.getAPIResource(rest,keyword,version)
    
    Ok(views.html.apis_list("resource_" + id, id, apiResource.apis))
  }
   
   def deleteVersion(version:String) = Action{
     if(StringUtil.isNotBlank(version)){
       versionTrackingService.deleteVersion(version)
       Ok("OK")
     }else{
       Ok("Failed")
     }
   }
   
   def getResourceNameInVersion(version:String) = Action {
     val list = versionTrackingService.getAPIREsourceListOfVersion(version)
     Ok(views.html.resources_list(list))
  }
   
  def searchAPI(version:String,keyword:String) = Action{
    Ok(apiResourceService.searchOperation(version,keyword).toString())
  }
}
