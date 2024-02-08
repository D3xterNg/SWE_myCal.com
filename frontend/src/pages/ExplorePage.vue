<script lang="ts">
import { mapStores } from 'pinia';
import { defineComponent } from 'vue';

import PropertyCard from 'components/PropertyCard.vue';
import Property from 'src/interface/Property';
import { usePropertyStore } from 'stores/property';
import { useSessionsStore } from 'stores/sessions';

export default defineComponent({
  name: 'ExplorePage',
  data() {
    return {
      properties: null as Property[][] | null,
      slide: 0,
      Price: null,
      flat_type: null,
      storey: 0,
      date: null,
      price_sel: ['< $300000', '$300001 - $500000', '$500001 - $700000', '$700001 - $900000', '> $900000'],
      flat_type_sel: ['2 Room Flexi', '3 Room', '4 Room', '5 Room', '3 Gen Flats'], //obtained from https://www.hdb.gov.sg/residential/buying-a-flat/finding-a-flat/types-of-flats
    }
  },
  components: { PropertyCard },
  computed: {
    ...mapStores(useSessionsStore, usePropertyStore)
  },
  async mounted() {
    /*let tempProperties = await this.propertyStore.getPropertyBySellerId("BRos1U3I9QNeGGDmWxo4");
    this.properties = [];
    while (tempProperties.length) {
      this.properties.push(tempProperties.splice(0, 3))
    }
  },*/
    try {
      let res = await this.propertyStore.getProperties();
      switch (typeof res) {
        case "object":
          this.properties = [];
          for (let i = 0; i < 3 && res.length > 0; i++) {
            this.properties.push(res.splice(0, 3));
          }
          break;
        case "number":
          if (res >= 500) {
            this.$q.notify({
              type: 'negative',
              message: 'Server Error'
            });
          }
          break;
      }
    } catch (error) {
      this.$q.notify({
            type: 'negative',
            message: 'Connection Error, PLease check your Network'
        });
    }
  },
  methods: {
    getImageUrl(src: string) {
      return new URL(src, import.meta.url).href
    },
    goToSearch() {
      this.$router.push({
        path: "/searchrental",
        query: {
          Price: this.Price,
          flat_type: this.flat_type,
          storey: this.storey,
          date: this.date,
        }
      })
    },
    sendIDToInfo(ID: string) {
      this.$router.push({
        path: "property/info",
        query: {
          id: ID,
        }
      }
      )
    }
  }
});

</script>

<template>
  <main class="main">
    <div class="container_1">
      <div>
        <div class="row" id="row_2">
          <div>
            <!-- <q-input standout v-model="Price" label="Price" class="selector" style="border-radius: 5px"></q-input> -->
            <q-select standout v-model="Price" :options="price_sel" label="Price" class="selector"></q-select>
          </div>
          <div>
            <q-select standout v-model="flat_type" :options="flat_type_sel" label="Flat-type" class="selector"
              style="border-radius: 5px"></q-select>
          </div>
          <div>
            <q-input standout v-model.number="storey" label="Storey" class="selector" inputmode="numeric" style="border-radius: 5px"></q-input>
          </div>
          <div>
            <q-input standout v-model="date" type="date" label="Date" class="selector" style="border-radius: 5px">

            </q-input>
          </div>
          <div>
            <q-btn push color="white" text-color="primary" label="Search" @click="goToSearch" class="search"></q-btn>
          </div>
        </div>
        <p v-if="storey < 0 || !Number.isInteger(storey)" class="error-message" style="color: red">
          Storey cannot have decimal or less than 1
        </p>
      </div>
    </div>
    <div class="container_2"></div>
    <div class="container_3">
      <div class="row" id="row_3">
        <p id="recently_added">Recently added</p>
        <div>
          <q-btn outline color="white" text-color="primary" label="View more"
            @click="$router.push('/searchrental')"></q-btn>
        </div>
      </div>
    </div>
    <div class="container_4">
      <q-inner-loading :showing="properties == null" label="Please wait..." label-class="text-teal"
        label-style="font-size: 1.1em" />
      <q-card>

        <q-carousel v-if="properties != null" swipeable animated v-model="slide" control-color="blue" navigation padding
          arrows height="500px" class="text-purple rounded-borders">
          <q-carousel-slide v-for="(property1, key) in properties" :name="key" class=" no-wrap">
            <div class="q-pa-md row items-start q-gutter-md" style="display: grid; grid-template-columns: 1fr 1fr 1fr;">
              <property-card v-for="(property2, value2) in property1" :key="value2" style="min-height: 100%;"
                :property-id="property2.id" :property="property2" @click="sendIDToInfo(property2.id)" />
            </div>
          </q-carousel-slide>
        </q-carousel>
      </q-card>
    </div>
  </main>
</template>

<style scoped>
main {
  display: flex;
  flex-direction: column;
  position: relative;
  flex: auto;
  width: 98vw;
}

#recently_added {
  font-size: 20px;
  width: 100%;
}

#row_3 {
  justify-content: space-between;
  min-width: calc(100% - 10rem);
  width: 80vw;
  margin-left: 5rem;
  margin-right: 5rem;
}

#row_2 {
  margin-left: 3rem;
  margin-right: 3rem;
  width: 100vh;
}

.row {
  flex-wrap: nowrap;
  max-width: 100vw;
}

.container_1 {
  display: flex;
  justify-content: center;
}

.container_1>div {
  background-image: url("assets/images/home_page_2.jpg");
  /* background-size: calc( 100% - 2rem ) 100%; */
  background-size: 100% 100%;
  background-repeat: no-repeat;

  width: 1350px;
  height: 400px;
  margin: 2px auto;
  position: relative;
}

.container_2 {
  min-height: 50px;
}

.search {
  display: flex;
  justify-content: space-around;
  margin: 0.5rem;
  min-width: 200px;
  min-height: 50px;
  background-color: white;
}

.selector {
  display: flex;
  justify-content: space-around;
  margin: 0.5rem;
  min-width: 250px;
  min-height: 50px;
  background-color: white;
}

.background_image {
  width: 101%;
  position: relative;
  margin-top: 0px;
  z-index: -10;
  top: 1rem;
  height: 500px;
}

.error-message {
  color: red;
  margin-left: 44%;
  font-weight: bold;
  font-size: small;
}
</style>
