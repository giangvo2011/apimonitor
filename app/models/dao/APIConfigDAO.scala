package models.dao
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.global._
import models.testcase.APIConfig
class APIConfigDAO extends MongoSalatDAO[APIConfig,String](APIConfig.getTableName){
  def parseValueParams(){
    
  }
}