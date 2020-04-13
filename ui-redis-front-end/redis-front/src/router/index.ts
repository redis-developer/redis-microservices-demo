import Vue from 'vue'
import VueRouter, { RouteConfig } from 'vue-router'
import Home from '../views/Home.vue'

Vue.use(VueRouter)

  const routes: Array<RouteConfig> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/About.vue')
  },
  {
    path: '/services',
    name: 'Services',
    component: () => import('../views/Services.vue')
  },
  {
    path: '/actors',
    name: 'Actors',
    component: () => import('../views/Actors.vue')
  },
  {
    path: '/actors/:id',
    name: 'ActorForm',
    component: () => import('../views/ActorForm.vue')
  },
  {
    path: '/movies',
    name: 'Movies',
    component: () => import('../views/Movies.vue')
  },
  {
    path: '/movies/:id',
    name: 'MovieForm',
    component: () => import('../views/MovieForm.vue')
  },

  {
    path: '/autocomplete/',
    name: 'Autocomplete',
    component: () => import('../views/Autocomplete.vue')
  },

  {
    path: '/search/actors',
    name: 'SearchActors',
    component: () => import('../views/SearchActors.vue')
  },

  {
    path: '/search/movies',
    name: 'SearchMovies',
    component: () => import('../views/SearchMovies.vue')
  },

  {
    path: '/aggregations/',
    name: 'Aggregations',
    component: () => import('../views/Aggregations.vue')
  },

  ]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
