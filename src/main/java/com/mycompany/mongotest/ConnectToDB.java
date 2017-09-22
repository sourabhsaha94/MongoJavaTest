/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mongotest;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
/**
 *
 * @author sosaha
 */
public class ConnectToDB {

    
    public static void main( String args[]){
    
        ConnectToDB connectToDB = new ConnectToDB();
        //used to authenticate
        MongoCredential mongoCredential = MongoCredential.createCredential("accountUser", "test", "password".toCharArray());
        MongoClient mongo = new MongoClient(new ServerAddress("localhost",27017),Arrays.asList(mongoCredential));
        
        //get data from collection
        MongoDatabase database = mongo.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("projects");
        
        System.out.println(connectToDB.getAllProjectsByUUID("1", collection).toString());
        System.out.println(connectToDB.getAllUUIDByProjectName("project 2", collection).toString());
        
        Map<String,Object> map = new HashMap<>();
        
        map.put("rhatUUID", "3");
        map.put("projectName", "project 4");
        map.put("startDate",new Date().toString());
        map.put("endDate", null);
        map.put("percentage",30);
        
        //connectToDB.insertMapintoDB(map, collection);
        System.out.println(connectToDB.getAllProjectsByUUID("3", collection).toString());
        
        map.clear();
        map.put("projectName", "project 6");
        map.put("endDate", new Date().toString());
        connectToDB.updateUUIDInfo("3",map, collection);
        
        System.out.println(connectToDB.getAllProjectsByUUID("3", collection).toString());
        
    }
    
    //MongoDB document created from hashmap and inserted into a collection
    public void insertMapintoDB(Map<String,Object> hashmap,MongoCollection collection){
        Document document = new Document(hashmap);
        collection.insertOne(document);
    }
    //$set is used to set the value of fields in hashmap to a particular document
    public void updateUUIDInfo(String uuid,Map<String,Object> hashmap, MongoCollection collection){
        Document document = new Document(hashmap);
        collection.updateMany(Filters.eq("rhatUUID",uuid),new Document("$set",document));
    }
    
    public ArrayList<String> getAllUUIDByProjectName(String projectName, MongoCollection collection){
        
        MongoCursor iterator = collection.find(Filters.eq("projectName", projectName)).iterator();
        
        ArrayList<String> listOfUUIDs = new ArrayList<>();
        
        while(iterator.hasNext()){
            Document document = (Document)iterator.next();
            listOfUUIDs.add(document.get("rhatUUID").toString());
        }
        
        return listOfUUIDs;
    }
    
    public ArrayList<Document> getAllProjectsByUUID(String uuid,MongoCollection collection){
        
        FindIterable<Document> allProjectsByUUID = collection.find(Filters.eq("rhatUUID", uuid));
        
        Iterator iterator = allProjectsByUUID.iterator();
        
        ArrayList<Document> list = new ArrayList<>();
        
        while(iterator.hasNext()){
            list.add((Document)iterator.next());
        }
        
        return list;
    }

}
