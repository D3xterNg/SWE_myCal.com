<template>
  <div class="jumbotron">
    <div>
    <q-form class="booking" @submit.prevent="confirm">
      <div class="text-center">
        <h3 class="heading">Booking</h3>
      </div>

      <div class="fields-container">
        <div class="picture">
          <div>
      <q-inner-loading :showing="properties == null" />
      <template v-if="!!properties && properties.length">
        <q-card>

          <property-list :property-id="(property?.id as string)"></property-list>
        </q-card>
      </template>
      <p v-if="!!properties && !properties.length" style="text-align: center">
        No Purchased Property
      </p>
    </div>
        </div>

        <div class="info">

        <div class="name">
          <q-input type="text" id="name" label="Name" style="width:497px;" v-model="bookingDetails.buyerName" /><br>
        </div>

        <div class="row">
          <q-input type="text" label="Contact Number" id="contact" name="contact" v-model="bookingDetails.buyerContactNumber" style="width: 220px;"
                   :rules="[val => !!val || 'Contact Number Required',
          val => (val && /^\d{8}$/.test(val)) || 'Must be an 8-digit number']"
          /><br>
        <div>
          <q-input type="date" id="date" name="date" label="Booking Date" style="width:217px;" :rules="[val =>!!val || 'Booking Date Required' ]"
            v-model="bookingDetails.bookingDate" /><br>
        </div>
        </div>
      </div>

      <div class="button">
        <q-btn class="cancel-button" @click="cancel">Cancel</q-btn>
        <q-btn class="update-button" type="submit">Confirm</q-btn>
      </div>
    </div>
  </q-form>
</div>
</div></template>

<script lang="ts">
import Property from 'src/interface/Property';
import PropertyList from 'src/components/PropertyList.vue';
import { mapStores } from 'pinia'
import { defineComponent } from 'vue';
import { usePropertyStore } from 'stores/property';
import { useAccountStore } from 'stores/accounts';
import { useSessionsStore } from 'stores/sessions';
import BookingDetails from 'src/interface/BookingDetails'

export default defineComponent({
  name: 'Booking',
  data() {
    return {
      search: "",
      properties: null as string[] | null,
      property: null as Property | null,
      propertyObj: null as Property[] | null,
      bookingDetails: {
        bookingDate: '',
        buyerContactNumber: '',
        buyerName: ''
      } as BookingDetails
    };
  },
  components:{PropertyList},

  computed: {
    ...mapStores(usePropertyStore, useAccountStore, useSessionsStore),

  },

  methods: {
    cancel() {
      this.$router.push('/property/info');
    },
    confirm() {
      const userId = this.sessionsStore.sessionAccount?.accountId
      console.log(userId)
      if (userId != null) {
        this.AccountStore.bookProperty(userId, this.property?.id as string, this.bookingDetails)
          .then((status) => {
            if (status === 204) {
              console.log('Property booked successfully');
              this.$router.push('/myresaleprop');
              this.$q.notify({
                type: 'positive',
                message: "Booking Successful"
              });
            } else {
              console.error('Error in booking property');
              this.$router.push('/explore');
              this.$q.notify({
                type: 'negative',
                message: "Booking Unsuccessful"
              });
            }
          })
          .catch((error) => {
            console.error('Error in saving property:', error);
            // Add any other error handling logic here
          });
      }


    }
  },

  async mounted() {
    if (this.sessionsStore.sessionAccount == null) {
      this.$router.replace('/login?from=' + encodeURIComponent('/property/manage'))
      return;
    }

    let tempProperties = await this.propertyStore.getPropertyByRecentlyAdded();
    while (tempProperties) {
      let tempProperty = tempProperties.pop();
      if (tempProperty != null && tempProperty.id == this.$route.query.property) {
        this.property = tempProperty;
        break
      }
    }
    console.log(this.property);
  },
  async created() {
    if (this.sessionsStore.sessionAccount == null) {
      this.$router.replace(
        "/login?from=" + encodeURIComponent("/myresaleprop")
      );
      return;
    }
    this.properties = await this.AccountStore.getBookedProperty(
      this.sessionsStore.sessionAccount.accountId
    );
    this.propertyObj = await this.propertyStore.getPropertiesByIds(
      this.properties
    );
  },
});
</script>

<style scoped>
.jumbotron {
  background-image: url("/statics/6.jpg");

}

.booking {
  margin: 0 auto;
  margin-bottom: 10%;
  /* Centered */
  padding: 15px;
  /* background-color: #ededed; */
  width: 100vh;
  height: 90vh;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;

}

.fields-container {
  padding: 10px;
  width: 100%;
  height: auto;
  background-color: rgb(234, 205, 167);
  box-shadow: inset 3px 0 8px -5px #d8d6d6, inset -3px 0 8px -5px #d8d6d6;
  border-radius:20px;
}

.picture {
  border: 100%;
  top: 100%;
}

.column {
  column-gap: 5%;
}

.button {
  margin-top: 10%;
  margin-left: 25%;
  margin-right: 25%;
  display: flex;
  justify-content: space-between;
}

.info {
  margin-top: 50px;
  margin-left: 15%;
  column-gap: 60px;
}

.name {
  margin-top: 50px;
  text-align: left;

}

.text-center {
  margin-left: 35%;
  color: white;
  font-family: sans-serif;
}

.row {
  text-align: left;
  column-gap: 60px;
}

/*.cancel-button {*/
/*  background-color: #dc3545;*/
/*}*/

/*.update-button {*/
/*  background-color: #007bff;*/
/*}*/</style>
