
<template>
  <div>

    
    <posts-list
      v-if="!isLoading"
      :posts="computedPosts"
    >
      <template slot-scope="post">
        <div>
          <figure class="flex items-center mb-3">
            <img 
              class="h-16 w-16 rounded-full mr-2"
              :src="`https://robohash.org/${post.title}`"
              :alt="post.title"
            >
            <figcaption>
              <h3 class="text-base">{{post.title}}</h3>
            </figcaption>
          </figure>
          <p class="text-grey-dark">{{post.body}}</p>
        </div>
      </template>
    </posts-list>
  </div>
</template>

<script>
import PostsList from './PostsList'
import { RepositoryFactory } from './../repositories/RepositoryFactory'
const PostsRepository = RepositoryFactory.get('posts')

export default {
  name: "parent-component",
  components: { PostsList },
  data() {
    return {
      isLoading: false,
      posts: [],
    };
  },
  created () {
    this.fetch()
  },
  methods: {
    async fetch () {
      this.isLoading = true
      const { data } = await PostsRepository.get()
      this.isLoading = false
      this.posts = data;
    }
  },
  computed: {
    computedPosts () {
      return this.posts.slice(0, 10)
    }
  }
};
</script>

