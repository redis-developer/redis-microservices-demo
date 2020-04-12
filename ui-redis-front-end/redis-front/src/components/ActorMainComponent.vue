
<template>
  <div>

    
    <records-list
      v-if="!isLoading"
      :records="computedRecords"
    >
      <template slot-scope="record">
          ---
        <div>
            <figcaption>
              <h3 class="text-base">{{record.first_name}} {{record.last_name}}</h3>
            </figcaption>
          <p class="text-grey-dark">{{record.body}}</p>
        </div>
      </template>
    </records-list>
  </div>
</template>

<script>
import RecordsList from './GenericObjectList'
import { RepositoryFactory } from './../repositories/RepositoryFactory'
const RDBMSRepository = RepositoryFactory.get('rdbmsRepository')

export default {
  name: "parent-component",
  components: { RecordsList },
  data() {
    return {
      isLoading: false,
      records: [],
    };
  },
  created () {
    this.fetch()
  },
  methods: {
    async fetch () {
      this.isLoading = true
      const { data } = await RDBMSRepository.findActors()
      this.isLoading = false
      this.records = data;
    }
  },
  computed: {
    computedRecords () {
      return this.records; 
    }
  }
};
</script>

