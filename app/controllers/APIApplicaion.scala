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
  
  def getapi(url: String) = Action {
    val latestVersion = versionTrackingService.getLastedVersion()
    val apis = versionTrackingService.getPathListOfVersion(latestVersion)
    var list = List[APIResource]()
    apis.foreach(api => {
      val id = api;
      println(id);
      list ::= apiResourceService.getAPIResource(id);
    })

    Ok(views.html.resources_list(list))
  }
  
  def getResources(start: String, size: String, rest: String) {
    var iStart = 0
    var iSize = 10
    if (StringUtil.isNotBlank(start)) {
      iStart = start.toInt
    }
    if (StringUtil.isNotBlank(size)) {
      iSize = size.toInt
    }
    
    apiResourceService.getAPIResources(iStart, iSize, rest,"")
    
  }
}
