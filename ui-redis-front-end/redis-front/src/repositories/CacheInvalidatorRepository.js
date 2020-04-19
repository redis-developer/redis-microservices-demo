import Repository from "./Repository";

// Check proxy configuration
const baseDomain = "";
const baseURL = `${baseDomain}`;
const resource = baseURL + "/api/caching/";
export default {

  async status() {
    let retValue = {};
    try {
      //retValue = 
      retValue = await Repository.get(`${resource}/status`);
    }
    catch(e) {
      console.log(`Service ${baseDomain} not available `);
      retValue.status = "ERROR";
    }
    return retValue;
  }, 

  start() {
    return Repository.get(`${resource}/start`);
  }, 

  stop() {
    return Repository.get(`${resource}/stop`);
  }, 

  getRatings(id, withCache) {
    let cache = 0;
    if (withCache) {
      cache=1;
    }
    return Repository.get(`${resource}/ratings/${id}?cache=${cache}`);
  }, 


  getServiceInfo(){
    return resource;
  }

};
