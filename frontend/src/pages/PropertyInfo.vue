<template>
  <div v-if="property">
    <q-img
    :src="`/statics/uploadedImage/property/${property.id}${property.photoExtension}`"
      contain:true
      height="60vh"
    >
      <div class="absolute-top-right">
        <div v-if="sessionsStore.isLogin">
      <div v-if="isPropertySaved === false">
        <q-avatar :loading="loading" icon = "star_border" @click="save" size="60px"></q-avatar>
      </div>
      <div v-if="isPropertySaved">
        <q-avatar :loading="loading" icon = "star" style="color: #FFD700;" @click="deleteSaveProperty" size="60px"></q-avatar>
      </div>
    </div>
      </div>
    </q-img>


    <div class="custom-division q-flex q-h-full q-items-center q-justify-center q-my-md">
    <!-- Your Quasar row of 4 cards -->
    <div class="row justify-center items-center">
      <q-card class="col-12 col-md-6 col-lg-2 q-mb-md q-pt-sm" style="margin-right: 15px; width: 20%">
        <q-card-section class="q-pt-none q-pb-none" style="height: 100%;">
          <div class="row items-center">
            <!-- Icon on the left -->
            <q-avatar icon="location_city" size="60px" class="q-mr-md"></q-avatar>

            <!-- Title and subtitle on the right -->
            <div class="q-flex flex-column">
              <div class="text-h6">Flat-Type</div>
              <div class="text-subtitle1 q-my-sm q-text-center">{{ property.flatType }}</div>
            </div>
          </div>
        </q-card-section>
      </q-card>

      <q-card class="col-12 col-md-6 col-lg-2 q-mb-md q-pt-sm" style="margin-right: 15px; width: 20%">
        <q-card-section class="q-pt-none q-pb-none" style="height: 100%;">
          <div class="row items-center">
            <!-- Icon on the left -->
            <q-avatar icon="king_bed" size="60px" class="q-mr-md"></q-avatar>

            <!-- Title and subtitle on the right -->
            <div class="q-flex flex-column">
              <div class="text-h6">Bedrooms</div>
              <div class="text-subtitle1 q-my-sm q-text-center">{{ property.numOfBedrooms }} Bedrooms</div>
            </div>
          </div>
        </q-card-section>
      </q-card>

      <q-card class="col-12 col-md-6 col-lg-2 q-mb-md q-pt-sm" style="margin-right: 15px; width: 20%">
        <q-card-section class="q-pt-none q-pb-none" style="height: 100%;">
          <div class="row items-center">
            <!-- Icon on the left -->
            <q-avatar icon="shower" size="60px" class="q-mr-md"></q-avatar>

            <!-- Title and subtitle on the right -->
            <div class="q-flex flex-column">
              <div class="text-h6">Bathrooms</div>
              <div class="text-subtitle1 q-my-sm q-text-center">{{ property.numOfBathrooms }} Bathrooms</div>
            </div>
          </div>
        </q-card-section>
      </q-card>

      <q-card class="col-12 col-md-6 col-lg-2 q-mb-md q-pt-sm" style="margin-right: 15px; width: 20%">
        <q-card-section class="q-pt-none q-pb-none" style="height: 100%;">
          <div class="row items-center">
            <!-- Icon on the left -->
            <q-avatar icon="house" size="60px" class="q-mr-md"></q-avatar>

            <!-- Title and subtitle on the right -->
            <div class="q-flex flex-column">
              <div class="text-h6">Size</div>
              <div class="text-subtitle1 q-my-sm q-text-center">{{ property.size }} sqm</div>
            </div>
          </div>
        </q-card-section>
      </q-card>


    </div>
  </div>

  <div class="row q-mx-xl q-mb-xl">
      <div class="col-12 col-md-8">
        <div class = "text-h5 q-mb-sm q-ml-lg">
          {{ property.title }}
        </div>
        <div class = "text-body1 q-ml-lg q-mb-md">
          {{ property.description }}
        </div>
        <div class="text-body1 q-ml-lg q-mb-lg">
          <q-icon name="location_pin"  style="color: #fe3232; font-size: 30px;" />
          this is the location
          <a :href="getGoogleMapLink(property.location)" target="_blank"
            >View on Google Maps</a
          >
        </div>

        <q-separator class = "q-mx-sm"/>

        <div class = "text-h5 q-ml-lg q-my-md text-center	">
          Starting from $S {{ property.price }}
        </div>
        <div class = "text-h6 q-ml-lg q-mt-sm text-center	">
          Available from {{ property.commenceDate }}
        </div>

        <q-separator class = "q-mx-sm q-mt-md"/>

      </div>
      <div class="col-12 col-md-4">
        <div class="card-section">
          <iframe
            :src="`https://maps.google.com/maps?q=${property.location}&t=&z=13&ie=UTF8&iwloc=&output=embed`"
            width="400"
            height="300"
            frameborder="0"
            allowfullscreen="allowfullscreen"
            style="border: 0px"
          ></iframe>
        </div>
      </div>
    </div>

    <div v-if="sessionsStore.isLogin" class="action-sections">

          <div class="row justify-center">
            <q-btn class = "q-my-xl" color="primary" size = md style="width: 500px" @click="book">Book</q-btn>

          </div>
      </div>
  </div>
  <q-inner-loading v-else :showing="true"></q-inner-loading>
</template>

<script lang="ts">
import { defineComponent } from "vue";
// import axios from 'axios';
import { mapStores } from "pinia";
import { usePropertyStore } from "stores/property";
import { useAccountStore } from "stores/accounts";
import { useSessionsStore } from "stores/sessions";
import Property from "src/interface/Property";
import { colors } from "quasar";

export default defineComponent({
  data() {
    return {
      property: null as Property | null,
      loading: false,
      isPropertySaved: null as null | boolean,
    };
  },
  computed: {
    ...mapStores(usePropertyStore, useSessionsStore, useAccountStore),
  },
  async mounted() {
    await this.getPropertyDetails();
  },
  methods: {
    async getPropertyDetails() {
      // const propertyId = await this.propertyStore.getCurrentPropertyId();
      // console.log(propertyId)
      let id = this.$route.query.id;
      if (typeof id !== "string") {
        this.$router.replace("/404");
        return;
      }
      try {
        this.property = await this.propertyStore.getProperty(id); // Make sure to type the response data
        if (this.sessionsStore.sessionAccount) {
          let status = await this.AccountStore.hasSavedProperty(
            this.sessionsStore.sessionAccount.accountId,
            id
          );
          switch (status) {
            case 204:
              this.isPropertySaved = true;
              break;
            case 404:
              this.isPropertySaved = false;
              break;
          }
        }
      } catch (error) {
        console.error("Error fetching property details", error);
      }
    },
    getGoogleMapLink(location: string) {
      return `https://www.google.com/maps/search/?api=1&query=${location}`;
    },
    book() {
      if (this.property != null) {
        this.$router.push({
          path: "/booking",
          query: {
            property: this.property.id,
          },
        });
      } else this.$router.push("/booking");
    },
    save() {
      const userId = this.sessionsStore.sessionAccount?.accountId;
      console.log(userId);
      this.loading = true;
      if (userId) {
        this.AccountStore.saveProperty(userId, this.property?.id as string)
          .then((status) => {
            if (status === 204) {
              console.log("Property saved successfully");
              this.isPropertySaved = true;
              // Add any other logic here for successful save
            } else {
              console.error("Error in saving property");
              // Add any other error handling logic here
            }
          })
          .catch((error) => {
            console.error("Error in saving property:", error);
            // Add any other error handling logic here
          })
          .finally(() => (this.loading = false));
      } else {
        console.error("User ID not found");
        // Add any other error handling logic here
      }
    },
    async deleteSaveProperty() {
      const userId = this.sessionsStore.sessionAccount?.accountId;
      if (!userId) return;
      try {
        this.loading = true;
        let status = await this.AccountStore.deleteSavedProperty(
          userId,
          this.property?.id as string
        );
        if (status === 204) {
          console.log("Property delete successfully");
          this.isPropertySaved = false;
          // Add any other logic here for successful save
        } else {
          console.error("Error in saving property");
          // Add any other error handling logic here
        }
      } catch (e) {
      } finally {
        this.loading = false;
      }
    },
  },
});
</script>
