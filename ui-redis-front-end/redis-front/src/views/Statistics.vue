<template>
  <div>


    <div>
    <h1> <b-icon-graph-up />  Redis Movies Database Stats</h1>
    </div>

    <b-container >


  <div>
    <b-form-group>
      <b-form-radio v-model="selectedStatType" name="some-radios" value="movieByYear">Movie by Year</b-form-radio>
      <b-form-radio v-model="selectedStatType" name="some-radios" value="movieByGenre">Movie by Genre</b-form-radio>
    </b-form-group>

  </div>

    </b-container>

    <div id="chardContainer">
        <svg id="barChart"></svg>
    </div>


  </div>
</template>

<script>
import { RepositoryFactory } from './../repositories/RepositoryFactory'
const RediSearchRepository = RepositoryFactory.get('dataStreamsRedisHashSyncRepository');

import * as d3 from "d3";
import _ from "lodash";

export default {
  data() {
    return {
        movieStats: {},
        selectedStatType: 'movieByYear',
        movieByYear : { title: 'Movie by Year' , xLabel: "Year", yLabel:"Movies" },
        movieByGenre : { title: 'Movie by Genre' , xLabel: "Genre", yLabel:"Movies" },
        optionStatType: [
          { value: 'movieByYear', text: 'Movie by Year'},
          { value: 'movieByGenre', text: 'Movie by Genre', xLabel: "Genre", yLabel:"Movies" },
        ]
    };
  },
  created() {

      this.fetch();
  },
  watch: {
    selectedStatType: function() {
      this.showStats();
    }
  },
  methods: {
      showStats(){
        this.renderChart(
            this.movieStats[this.selectedStatType].results,
            this[this.selectedStatType].title,
            this[this.selectedStatType].xLabel,
            this[this.selectedStatType].yLabel
            );

      this.$parent.contextualHelp = 
      `RediSearch aggregation query used to generate this chart: <hr><p><small>${this.movieStats[this.selectedStatType].query }</small></p><hr>`+
      `You can copy and paste the query in Redis Insight to test it `;

      },
      fetch() {
          this.getStats()
      },
    async getStats() {
        const { data } = await RediSearchRepository.getMovieStats();
        this.movieStats = data;
        this.showStats();
    },
    renderChart(stats, title, xLabel, yLabel) {
      const margin = 60;
      const svg_width = 1000;
      const svg_height = 600;
      const chart_width = 1000 - 2 * margin;
      const chart_height = 600 - 2 * margin;
      
      // delete all chart elements before drawing again
      d3.selectAll("g > *").remove(); // TODO: Fix this since it removes the icon in the title (remove h)
      d3.select("#chartTitle").remove();
      d3.select("#yLabel").remove();
      d3.select("#xLabel").remove();

      const svg = d3
        .select( "#barChart" )
        .attr("width", svg_width)
        .attr("height", svg_height);
      
      this.chart = svg
        .append("g")
        .attr("transform", `translate(${margin}, ${margin})`);
      
      const yScale = d3
        .scaleLinear()
        .range([chart_height, 0])
        .domain([0, _.maxBy(stats, "value").value]);
      this.chart
        .append("g")
        .call(d3.axisLeft(yScale).ticks())
        ;
      const xScale = d3
        .scaleBand()
        .range([0, chart_width])
        .domain(stats.map(s => s.key))
        .padding(0.2);

      this.chart
        .append("g")
        .attr("transform", `translate(0, ${chart_height})`)
        .call(d3.axisBottom(xScale))
        .selectAll("text")
        .attr("y", 0)
        .attr("x", 9)
        .attr("dy", ".35em")
        .attr("transform", "rotate(90)")
        .style("text-anchor", "start");

        const barGroups = this.chart
            .selectAll("rect")
            .data(stats)
            .enter();
      
      barGroups
        .append("rect")
        .attr("class", "bar")
        .attr("x", g => xScale(g.key))
        .attr("y", g => yScale(g.value))
        .attr("height", g => chart_height - yScale(g.value))
        .attr("width", xScale.bandwidth()
        );

      svg
        .append("text")
        .attr("id", "xLabel")
        .attr("class", "label")
        .attr("x", -(chart_height / 2) - margin)
        .attr("y", margin / 2.4)
        .attr("transform", "rotate(-90)")
        .attr("text-anchor", "middle")
        .text(yLabel);
      
      svg
        .append("text")
        .attr("class", "label")
        .attr("id", "yLabel")
        .attr("x", chart_width / 2 + margin)
        .attr("y", chart_height + margin * 2)
        .attr("text-anchor", "middle")
        .text(xLabel);
      
      svg
        .append("text")
        .attr("class", "title")
        .attr("id", "chartTitle")        
        .attr("x", chart_width / 2 + margin)
        .attr("y", 40)
        .attr("text-anchor", "middle")
        .text(title);
    }
  }
};
</script>

<style>
.bar {
  fill: #319bbe;
}
</style>