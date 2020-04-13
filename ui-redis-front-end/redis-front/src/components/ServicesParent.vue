
<template>

<div>

  <h3>Microservices Status</h3>
  <hr/>

  <div>
    <b-card-group deck>
      <b-card
        title="RDBMS to Redis Streams"
        img-src="/imgs/cdc-service-1.png"
        img-alt="Image"
        img-top
        tag="article"
        align="center"
      >
        <b-card-text>
          <div>
          This service uses CDC to capture database transactions and send them to <b>Redis Streams</b>.
          The list of tables, and database locations are in the <i>application.properties</i> file
          </div>      
        </b-card-text>

        <b-button @click="DbToStreamsChangeStatus"  >
          {{(streamProducerServiceStatus.call.status == "RUNNING")?"Stop It!":"Start It!"  }}
        </b-button>


        <template v-slot:footer>
          <b-alert :variant="streamProducerServiceStatus.messageColor" show>
            Status : {{streamProducerServiceStatus.call.status}} 
          </b-alert>
        </template>
      </b-card>

      <b-card
        title="Auto-Complete"
        img-src="/imgs/autocomplete-service.png"
        img-alt="Image"
        img-top
        tag="article"
        align="center"
      >
        <b-card-text>
          <div>
          This service reads events from <b>Redis Streams</b> using various Consumer Groups, and update <b>RediSearch</b> index/suggestions. 
          Configuration in <i>application.properties</i> file
          </div>                
        </b-card-text>

        <div class="text-justify">
          <hr/>
          <b-form-group label="Redis Search Services">
            <b-form-checkbox-group
              id="autoCompleteOptionSelector"
              v-model="autoCompleteSelected"
              :options="autoCompleteOptions"
              switches
              stacked
            ></b-form-checkbox-group>

            <b-form-checkbox-group
              id="ftsOptionSelector"
              v-model="ftsSelected"
              :options="ftsOptions"
              switches
              stacked
            ></b-form-checkbox-group>

          </b-form-group>
        </div>


        <b-button @click="streamsToAutocompleteChangeStatus"  >
          {{(streamAutoCompleteServiceStatus.call.status == "RUNNING")?"Stop It!":"Start It!"  }}
        </b-button>


        <template v-slot:footer>
          <b-alert :variant="streamAutoCompleteServiceStatus.messageColor" show>
            Status : {{streamAutoCompleteServiceStatus.call.status}} 
          </b-alert>
        </template>
      </b-card>


      <b-card
        title="RedisGraph"
        img-src="/imgs/graph-service.png"
        img-alt="Image"
        img-top
        tag="article"
        align="center"
      >
        <b-card-text>
          <div>
          This service reads events from <b>Redis Streams</b> using various Consumer Groups, and update <b>RediGraph</b> allowing powerful data navigation and queries. 
          Configuration in <i>application.properties</i> file
          </div>      
        </b-card-text>

        <b-button @click="streamsToGraphChangeStatus"  >
          {{(streamsToGraphServiceStatus.call.status == "RUNNING")?"Stop It!":"Start It!"  }}
        </b-button>

        <template v-slot:footer>
          <b-alert :variant="streamsToGraphServiceStatus.messageColor" show>
            Status : {{streamsToGraphServiceStatus.call.status}} 
          </b-alert>
        </template>
      </b-card>

    <b-card
        title="Redis Data Structures"
        img-src="/imgs/cdc-service-2.png"
        img-alt="Image"
        img-top
        tag="article"
        align="center"
      >
        <b-card-text>
          <div>
          This service uses CDC to capture database transactions and send them to <b>Redis</b>.
          This is used to cache data, and provide fast access to business data.
          The list of tables, and database locations are in the <i>application.properties</i> file
          </div>      
        </b-card-text>

        <b-button @click="cacheServiceStatusChangeStatus"  >
          {{(cacheServiceStatus.call.status == "RUNNING")?"Stop It!":"Start It!"  }}
        </b-button>

        <template v-slot:footer>
          <b-alert :variant="cacheServiceStatus.messageColor" show>
            Status : {{cacheServiceStatus.call.status}} 
          </b-alert>
        </template>
      </b-card>

    <b-card
        title="Legacy API"
        img-src="/imgs/rdbms-service.png"
        img-alt="Image"
        img-top
        tag="article"
        align="center"
      >
        <b-card-text>
          <div>
          This service expose the SQL Database as REST API.
          It is also possible to generates some operations dynamically to see the impact on Redis Streams
          </div>      
        </b-card-text>

        <b-button @click="rdbmsServiceStatusChangeStatus"  >
          {{(rdbmsServiceStatus.call.status == "RUNNING")?"Stop It!":"Start It!"  }}
        </b-button>

        <template v-slot:footer>
          <b-alert :variant="rdbmsServiceStatus.messageColor" show>
            Status : {{rdbmsServiceStatus.call.status}} 
          </b-alert>
        </template>
      </b-card>
    </b-card-group>
  </div>  
</div>
</template>

<script>
import { RepositoryFactory } from './../repositories/RepositoryFactory'
const RDBMSRepository = RepositoryFactory.get('rdbmsRepository')
const DataStreamsToGraphRepository = RepositoryFactory.get('dataStreamsToGraphRepository');
const DataStreamsProducerRepository = RepositoryFactory.get('dataStreamsProducerRepository');
const DataStreamsAutoCompleteRepository = RepositoryFactory.get('dataStreamsAutoCompleteRepository');
const CacheInvalidatorRepository = RepositoryFactory.get('cacheInvalidatorRepository')


export default {
  name: "parent-component",
  data() {
    return {
      streamProducerServiceStatus: {info:null,call:{status : "ERROR"},messageColor:""},
      streamAutoCompleteServiceStatus: {info:null,call:{status : "ERROR", "fulltext": "false", "suggest": "false" },messageColor:""},
      streamsToGraphServiceStatus: {info:null,call:{status : "ERROR"},messageColor:""},
      cacheServiceStatus: {info:null,call:{status : "ERROR"},messageColor:""},
      rdbmsServiceStatus: {info:null,call:{status : "ERROR"},messageColor:""},
      ftsSelected: [],
      ftsOptions:  [{ text: 'Full Text', value: 'fulltext' }],
      autoCompleteSelected: [],
      autoCompleteOptions:  [{ text: 'Autocomplete', value: 'autocomplete' }]
      
    
    };
  },
  created () {
    setInterval(function (){
      this.fetch();
    }.bind(this), 1500);

  },
  methods: {
    async fetch () {


      try {

        let { data } = await RDBMSRepository.status()
        if (data != undefined) {
          this.rdbmsServiceStatus.call = data;
        } else  {
          this.rdbmsServiceStatus.call = { status : "ERROR" };
        }
        this.rdbmsServiceStatus.info = RDBMSRepository.getServiceInfo();
        if (this.rdbmsServiceStatus.call.status == "RUNNING") {
        this.rdbmsServiceStatus.messageColor = "success";
        } else if (this.rdbmsServiceStatus.call.status == "ERROR") {
        this.rdbmsServiceStatus.messageColor = "danger";
        }  else {
        this.rdbmsServiceStatus.messageColor = "warning";
        } 
        

        data = await CacheInvalidatorRepository.status()
        if (data.data != undefined) {
          this.cacheServiceStatus.call = data.data;
        } else  {
          this.cacheServiceStatus.call = { status : "ERROR" };
        }
        this.cacheServiceStatus.info = CacheInvalidatorRepository.getServiceInfo();
        if (this.cacheServiceStatus.call.status == "RUNNING") {
        this.cacheServiceStatus.messageColor = "success";
        } else if (this.cacheServiceStatus.call.status == "ERROR") {
        this.cacheServiceStatus.messageColor = "danger";
        }  else {
        this.cacheServiceStatus.messageColor = "warning";
        }  

        data = await DataStreamsProducerRepository.status();
        if (data.data != undefined) {
          this.streamProducerServiceStatus.call = data.data;
        } else  {
          this.streamProducerServiceStatus.call = { status : "ERROR" };
        }
        this.streamProducerServiceStatus.info = DataStreamsProducerRepository.getServiceInfo();
        if (this.streamProducerServiceStatus.call.status == "RUNNING") {
        this.streamProducerServiceStatus.messageColor = "success";
        } else if (this.streamProducerServiceStatus.call.status == "ERROR") {
        this.streamProducerServiceStatus.messageColor = "danger";
        }  else {
        this.streamProducerServiceStatus.messageColor = "warning";
        }


        data = await DataStreamsAutoCompleteRepository.status()
        if (data.data != undefined) {
          this.streamAutoCompleteServiceStatus.call = data.data;
        } else  {
          this.streamAutoCompleteServiceStatus.call = {status : "ERROR", "fulltext": "false", "suggest": "false" };
        }
        this.streamAutoCompleteServiceStatus.info = DataStreamsAutoCompleteRepository.getServiceInfo();
        if (this.streamAutoCompleteServiceStatus.call.status == "RUNNING") {
        this.streamAutoCompleteServiceStatus.messageColor = "success";
        } else if (this.streamAutoCompleteServiceStatus.call.status == "ERROR") {
        this.streamAutoCompleteServiceStatus.messageColor = "danger";
        }  else {
        this.streamAutoCompleteServiceStatus.messageColor = "warning";
        }

        // print the status of the Search features (autocomplete/fts)
        if (this.streamAutoCompleteServiceStatus.call.suggest == "true") {
          this.autoCompleteSelected = ["autocomplete"];
        } else {
          this.autoCompleteSelected = [];
        }
        if (this.streamAutoCompleteServiceStatus.call.fulltext == "true") {
          this.ftsSelected = ["fulltext"];
        } else {
          this.ftsSelected = [];
        }


        data = await DataStreamsToGraphRepository.status()
        if (data.data != undefined) {
          this.streamsToGraphServiceStatus.call = data.data;
        } else  {
          this.streamsToGraphServiceStatus.call = { status : "ERROR" };
        }
        this.streamsToGraphServiceStatus.info = DataStreamsToGraphRepository.getServiceInfo();
        if (this.streamsToGraphServiceStatus.call.status == "RUNNING") {
        this.streamsToGraphServiceStatus.messageColor = "success";
        } else if (this.streamsToGraphServiceStatus.call.status == "ERROR") {
        this.streamsToGraphServiceStatus.messageColor = "danger";
        }  else {
        this.streamsToGraphServiceStatus.messageColor = "warning";
        } 

      } catch (err ) {
        console.log(err);
      }

    },
    async DbToStreamsChangeStatus() {
      if (this.streamProducerServiceStatus.call.status == "RUNNING") {
        await DataStreamsProducerRepository.stop()
      } else {
        await DataStreamsProducerRepository.start()
      }
      this.streamProducerServiceStatus.call.status = "...";
    },
    async streamsToAutocompleteChangeStatus() {
      if (this.streamAutoCompleteServiceStatus.call.status == "RUNNING") {
        await DataStreamsAutoCompleteRepository.stop()
      } else {
        await DataStreamsAutoCompleteRepository.start()
      }
      this.streamAutoCompleteServiceStatus.call.status = "...";
    },
    async streamsToGraphChangeStatus() {
      if (this.streamsToGraphServiceStatus.call.status == "RUNNING") {
        await DataStreamsToGraphRepository.stop()
      } else {
        await DataStreamsToGraphRepository.start()
      }
      this.streamsToGraphServiceStatus.call.status = "...";
    },
    async cacheServiceStatusChangeStatus() {
      if (this.cacheServiceStatus.call.status == "RUNNING") {
        await CacheInvalidatorRepository.stop()
      } else {
        await CacheInvalidatorRepository.start()
      }
      this.cacheServiceStatus.call.status = "...";
    },
    async rdbmsServiceStatusChangeStatus() {
      if (this.rdbmsServiceStatus.call.status == "RUNNING") {
        await RDBMSRepository.stop()
      } else {
        await RDBMSRepository.start()
      }
      this.rdbmsServiceStatus.call.status = "...";
    }
  }
};
</script>
