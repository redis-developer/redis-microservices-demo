<template>
  <div>
    <b-table
      sticky-header="400px"
      striped
      hover
      v-if="!isLoading"
      :items="computedRecords"
      @row-clicked="clickRow"
    >

   </b-table>

  <!-- <div class="justify-content-center row my-2">
    <b-pagination size="md" :total-rows="1000" :per-page="10" v-model="currentPage" />
  </div> -->

  </div>
</template>

<script>
import { RepositoryFactory } from './../repositories/RepositoryFactory'
const RDBMSRepository = RepositoryFactory.get('rdbmsRepository')

export default {
  name: "parent-component",
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
      const { data } = await RDBMSRepository.findMovies()
      this.isLoading = false
      this.records = data;
    },
    clickRow(record, index) {
      this.$router.push({ name: 'MovieForm', params: { id : record.movie_id } })
      console.log( ` ${index} -- ${record} `  );
    }
},
  computed: {
    computedRecords () {
      return this.records; 
    }
  }
};
</script>