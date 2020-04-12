import Repository from "./Repository";

// You can use your own logic to set your local or production domain
const baseDomain = "http://localhost:8085";
// The base URL is empty this time due we are using the jsonplaceholder API
const baseURL = `${baseDomain}`;

const resource = baseURL + "/api/1.0/data-streams-to-autocomplete/";
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


  getServiceInfo(){
    return resource;
  }



};
