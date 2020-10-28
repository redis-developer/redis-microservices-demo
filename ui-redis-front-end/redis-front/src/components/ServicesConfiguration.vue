
<template>

<div>
    <h3>Configuration</h3>

    <div class="container mt-4">
        <b-row>
            <div class="col-sm">
                <div class="input-group ">
                    <label for="colFormLabelSm" class="col-sm-3 col-form-label col-form-label-sm">OMDB API Key</label>
                    <b-form-input v-model="omdbApiKey"  placeholder="OMDB API Key" aria-label="OMDB API Key" aria-describedby="saveOmdbKey"></b-form-input>
                    <div class="input-group-append">
                        <b-button class="btn btn-outline" type="button" id="saveOmdbKey" @click="saveOmdbApiKey">Save</b-button>
                    </div>
                </div>
                <span class="small">Total Number of calls : {{omdbApiCalls}} </span>
            </div>
            <div class="col-sm">
            </div>
        </b-row>

        <b-row  class="mt-3">
            <div class="col-sm">
            </div>
            <div class="col-sm">
                <b-alert
                    :show="dismissCountDown"
                    dismissible
                    @dismissed="dismissCountDown=0"
                    @dismiss-count-down="countDownChanged"
                    >
                    Configuration saved.
                </b-alert>
            </div>
            <div class="col-sm">
            </div>
        </b-row>


    </div>




</div>
</template>

<script>
import { RepositoryFactory } from './../repositories/RepositoryFactory'
const CacheInvalidatorRepository = RepositoryFactory.get('cacheInvalidatorRepository')

export default {
    name: "configuration-component",
    data() {
        return {
            omdbApiKey : null,
            omdbApiCalls : 0,
            dismissSecs: 4,
            dismissCountDown: 0,
            showDismissibleAlert: false
        }
    },
    created () {
        this.fetch();
    },
    methods: {
        async fetch() {
            const { data } = await CacheInvalidatorRepository.getOmdbApiStats();
            this.omdbApiKey = data.OMDB_API_KEY;
            this.omdbApiCalls = data.OMDB_API_CALLS;

        },
        async saveOmdbApiKey(){
            await CacheInvalidatorRepository.saveOmdbApiKeyRatings(this.omdbApiKey)
            this.showAlert();
        },
        countDownChanged(dismissCountDown) {
        this.dismissCountDown = dismissCountDown
      },
      showAlert() {
        this.dismissCountDown = this.dismissSecs
      },
    }
};
</script>
