<template>
  <div>
    <div
      v-for="post in posts"
      v-bind="{post}"
      :key="post.id"
    >
      <slot v-bind="post"></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'posts-list',
  props: {
    posts: {
      type: Array,
      required: true,
    }
  }
}
</script>
