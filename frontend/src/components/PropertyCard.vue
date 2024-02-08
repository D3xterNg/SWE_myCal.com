<template>
  <q-card style="position: relative;"  class="cursor-pointer q-hoverable" @click="$router.push({path: '/property/info', query: { id: propertyId }})">
    <q-inner-loading :showing="property2 == null">
    </q-inner-loading>
    <template v-if="!!property2">
      <div class="row items-start">


        <q-btn v-if="deleteMode" round color="red" icon="delete" style="margin-right:auto"  @click.stop="$emit('deleteClick')"/>
        <div style="margin-right:auto"></div>
        <q-btn v-if="editMode" round color="amber" icon="edit"  @click.stop="$emit('editClick')" />


      </div>
      <img :src="`/statics/uploadedImage/property/${property2.id}${property2.photoExtension}`"
        style="max-width: 100%; height: auto; max-height: 250px;" />
      <q-card-section>
        <div class="text-h6">{{ property2.title }}</div>
      </q-card-section>
      <q-card-section class="q-pt-none">
        {{ property2.description }}
      </q-card-section>
    </template>
  </q-card>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { mapStores } from 'pinia'

import Property from 'src/interface/Property'
import { usePropertyStore } from 'stores/property'

export default defineComponent({
   name: 'PropertyCard',
  data() {
    return {
      property2: null as Property | null
    }
  },
  emits: ['deleteClick', 'editClick']
  ,
  props: {

    propertyId: {
      type: String,
      required: true
    },
    deleteMode:{
      type: Boolean,
      default: false
    },
    editMode: {
      type: Boolean,
      default: false
    },
    property: {
      type: Object as () => Property,
      required: false
    }
  },
  async created() {
    if (this.property == null) {
      this.property2 = await this.propertyStore.getProperty(this.propertyId)

    } else {
      this.property2 = this.property
    }
  },
  computed: {
    ...mapStores(usePropertyStore)
  }
})
</script>
