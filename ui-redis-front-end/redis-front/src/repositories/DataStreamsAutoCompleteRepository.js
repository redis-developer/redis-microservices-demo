import Repository from "./Repository";

// look in the proxy configuration for mapping
const baseDomain = "";
const baseURL = `${baseDomain}`;
const resource = baseURL + "/api/fulltext/";
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

  autocomplete(item, term) {
    return Repository.get(`${resource}/${item}/autocomplete?q=${term}`);
  }, 

  search(item, term) {
    return Repository.get(`${resource}/search/${item}?q=${term}`);
  }, 


  changeConfigFlag(config) {
    return Repository.get(`${resource}/config/${config}`);
  }, 


  getServiceInfo(){
    return resource;
  }



};
