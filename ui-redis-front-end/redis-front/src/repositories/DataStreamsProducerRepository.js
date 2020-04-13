import Repository from "./Repository";

// CHeck proxy config
const baseDomain = "";
const baseURL = `${baseDomain}`;
const resource = baseURL + "/api/data-to-streams";
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

  getServiceInfo(){
    return resource;
  }



};
