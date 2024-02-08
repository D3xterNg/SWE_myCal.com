<template>
  <div class="property-list">
      <q-inner-loading :showing="property == null">
    </q-inner-loading>
    <template v-if="!!property">

              <q-card-section>
                <q-list separator >
                  <q-item clickable v-ripple @click="$router.push({path: '/property/info', query: { id: propertyId }})">
                    <q-item-section thumbnail class="q-ml-none">
                      <img
                      img :src="`/statics/uploadedImage/property/${property.id}${property.photoExtension}`">
                    </q-item-section>

                    <q-item-section>
                      <q-item-label class="text-h6"
                        >{{ property.title }}</q-item-label
                      >
                      <q-item-label class="text-subtitle2"
                        >{{ property.location }}</q-item-label
                      >
                      <div>
                        <div class="row">
                          <div class="q-pa-sm">
                            <q-card class="my-card">
                              <q-card-section outlined>
                                <div>{{ property.numOfBedrooms }} Bedrooms</div>
                              </q-card-section>
                            </q-card>
                          </div>

                          <div class="q-pa-sm">
                            <q-card class="my-card"  >
                              <q-card-section outlined>
                                <div>{{ property.numOfBathrooms }} Bathrooms</div>
                              </q-card-section>
                            </q-card>
                          </div>

                          <div class="q-pa-sm">
                            <q-card class="my-card"  >
                              <q-card-section outlined>
                                <div>{{ property.size }} sqft</div>
                              </q-card-section>
                            </q-card>
                          </div>
                        </div>
                      </div>
                    </q-item-section>

                    <q-item-section side>
                      <q-separator color="primary" vertical inset />
                      <q-item-label class = "text-weight-light">Commencement</q-item-label>
                      <q-item-label class = "text-h5">{{ formattedDate }}</q-item-label>
                      <q-item-label class = "text-weight-light">{{ property.commenceDate.slice(0,4) }}</q-item-label>
                    </q-item-section>
                  </q-item>

                </q-list>
              </q-card-section>
              <q-separator spaced />

    </template>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { mapStores } from 'pinia'

import Property from 'src/interface/Property'
import { usePropertyStore } from 'stores/property'

export default defineComponent({
   name: 'PropertyList',
  data() {
    return {
      property: null as Property | null
    }
  },
  props: {
    propertyId: {
      type: String,
      required: true
    },
    property: {
      type: Object as () => Property,
      required: false
    }
  },
  async created() {
    if (this.property == null) {
      this.property = await this.propertyStore.getProperty(this.propertyId)

    } else {
      this.property = this.property
    }
  },
  computed: {
    ...mapStores(usePropertyStore),
    formattedDate() {
    const commenceDate = this.property.commenceDate;
    if (!commenceDate) {
      return ''; // Handle empty or invalid commenceDate
    }

    const [year, month, day] = commenceDate.split('-');
    const monthNames = [
      "Jan", "Feb", "Mar", "Apr", "May", "June",
      "July", "Aug", "Sep", "Oct", "Nov", "Dec"
    ];
    const monthName = monthNames[parseInt(month, 10) - 1];
    //const dayName = parseInt(day)+1;
    return `${monthName} ${day}`;
  }
  }

})
</script>

<style scoped>
img{
          width: 150px;
          height: 120px;
        }
</style>





