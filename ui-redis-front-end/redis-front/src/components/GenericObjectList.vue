<template>
  <div>

    <div
      v-for="record in records"
      v-bind="{record}"
      :key="record.id"
    >
      <slot v-bind="record"></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'records-list',
  props: {
    records: {
      type: Array,
      required: true,
    }
  }
}
</script>
