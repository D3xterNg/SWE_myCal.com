<template>
  <div class="home-section">
    <nav class="navbar">
      <!-- <div class="left-section">
        <div class="logo">
          <img class="circular-image" src="/assets/images/images2/logo1.png" alt="Country" />
        </div>
        <div class="nav-item" @mouseover="hoverNavItem = 0" @mouseleave="hoverNavItem = -1">
          <i class="fas fa-compass action-icon" v-if="hoverNavItem === 0"></i>
          Explore
        </div>
        <div class="nav-item" @mouseover="hoverNavItem = 1" @mouseleave="hoverNavItem = -1">
          <i class="fas fa-home action-icon" v-if="hoverNavItem === 1"></i>
          My Resale Properties
        </div>
        <div class="nav-item" @mouseover="hoverNavItem = 2" @mouseleave="hoverNavItem = -1">
          <i class="fas fa-cogs action-icon" v-if="hoverNavItem === 2"></i>
          Manage Properties
        </div>
        <div class="nav-item" @mouseover="hoverNavItem = 3" @mouseleave="hoverNavItem = -1">
          <i class="fas fa-calendar action-icon" v-if="hoverNavItem === 3"></i>
          Date Filter
        </div>
        <div class="logo">
          <img class="country" src="/assets/singapore.png" alt="Country" />
        </div>
      </div>
      <div class="right-section">
        <div class="logo">
          <img class="action-button" src="/assets/action.png" alt="Menu" />
          <img class="profile-image" src="/assets/profileP.png" alt="Profile" />
        </div>
      </div> -->
    </nav>
    <div class="content-container">
      <div class="search-bars">

        <!-- <q-select  bg-color="white" v-model="q_flat_type" :options="['all', '2 Room Flexi', '3 Room','4 Room','5 Room','3 Gen Flats']" label="Flat-type"  class="search-input" style="border-radius: 5px"></q-select>
        <q-input bg-color="white"  type="number" label="storey" class="search-input" v-model="q_storey" style="border-radius: 5px" />
        <q-input bg-color="white" filled  type="text" label="Price" class="search-input" v-model="q_Price" style="border-radius: 5px" />
        <q-input bg-color="white" filled type="date" class="search-input" v-model="q_date" style="border-radius: 5px" />
        <button class="search-button" @click="search">Search</button> -->
        <div class="row" id="row_2">
        <q-select standout v-model="q_Price" :options="price_sel" label="Price" class="selector"></q-select>
            <q-select standout v-model="q_flat_type" :options="flat_type_sel" label="Flat-type" class="selector"></q-select>
            <q-input standout v-model.number="q_storey" label="Storey" class="selector"></q-input>
            <q-input standout v-model="q_date" type="date" label="Date" class="selector"></q-input>
          <q-btn push color="white" text-color="primary" label="Search" @click="search" class="search"></q-btn>
        </div>
      </div>
      <p v-if=" q_storey <0 || !Number.isInteger(q_storey) " class="error-message" style="color: red">
           Storey cannot have decimal or less than 1
         </p>
    </div>
    <!-- <div class="background-image" :style="{ backgroundImage: `url(${'/assets/cover6.png)})` }">
      <h1 class="heading">Home to Sengkang</h1>
    </div> -->
    <q-inner-loading  :showing="loading" />
    <div style="display: grid; grid-template-columns: 1fr 1fr 1fr ;grid-gap: 1rem;" v-if="!loading">
      <property-card
      v-for="(property, index) in properties"
        :property="property"
        :propertyId="property.id"
    />
    </div>
  </div>
</template>

<script lang="ts">
import { mapStores } from "pinia";
import { defineComponent } from "vue";

import PropertyCard from "components/PropertyCard.vue";
import { usePropertyStore } from "stores/property";

import Property from "src/interface/Property";
import { store } from "quasar/wrappers";
export default defineComponent({
  name: "SearchRentalProperty",
  components: {
    PropertyCard,
  },
  data() {
    return {
      loading: false,
      Price:null,
      flat_type_sel: ['2 Room Flexi', '3 Room','4 Room','5 Room','3 Gen Flats'],
      price_sel: ['< $300000', '$300000 - $500000', '$500001 - $700000', '$700001 - $900000','> $900000' ],
      //query entries
      q_Price: '',
      q_flat_type: '',
      q_storey: 0,
      q_date: '',

      hoverNavItem: 0,
      properties: [
        // {
        //   title: "RiverTree's Residence",
        //   subTitle: "23 Elmo's Road",
        //   price: "$S 4,800,000",
        //   availableDate: "Available from 28 June 2023",
        //   details: "2 Bedrooms, Bathroom, 612 sqft",
        //   distance: "3.2 kms from Exit B of Sengang MRT",
        //   postedBy: "Ethan L.",
        //   imageUrl: '/statics/first.jpeg',
        // },
        // {
        //   title: "Greenvale Town",
        //   subTitle: "Rose Strept 805",
        //   price: "S$ 5.200.000",
        //   availableDate: "Available",
        //   details: "3 Bedrooms, 2 Bathroom, 789 sgft",
        //   distance: "43 kms from Exit A of Sengkang MRT",
        //   postedBy: "Jessie H",
        //   imageUrl: '/assets/second.jpeg',
        // },
        // {
        //   title: "RiverTree's Residence",
        //   subTitle: "23 Elmo's Road",
        //   price: "$S 4,800,000",
        //   availableDate: "Available from 28 June 2023",
        //   details: "2 Bedrooms, Bathroom, 612 sqft",
        //   distance: "3.2 kms from Exit B of Sengang MRT",
        //   postedBy: "Ethan L.",
        //   imageUrl: '/assets/first.jpeg',
        // },
        // {
        //   title: "Greenvale Town",
        //   subTitle: "Rose Strept 805",
        //   price: "S$ 5.200.000",
        //   availableDate: "Available",
        //   details: "3 Bedrooms, 2 Bathroom, 789 sgft",
        //   distance: "43 kms from Exit A of Sengkang MRT",
        //   postedBy: "Jessie H",
        //   imageUrl: '/assets/second.jpeg',
        // },
      ] as Property[],
    };
  },
  created() {
  this.q_Price=this.$route.query.Price as string || "";
  this.q_flat_type=this.$route.query.flat_type as string || 'all';
  this.q_storey= Number(this.$route.query.storey as string || 0);
  this.q_date=this.$route.query.date as string || "";

  console.log(this.q_Price);
  console.log(this.q_flat_type);
  console.log(this.q_storey);
  console.log(this.q_date);
  this.search();
/*   if (this.Price) {
    this.search()
  } */
},
  methods: {
    async search() {

      try {
        this.loading = true;
        console.log(this.q_date)

        let res = await this.propertyStore.getProperties(this.q_Price, this.q_flat_type, this.q_storey, this.q_date);
        switch (typeof res) {
          case "object":
              this.properties = res;
            break;
         case "number":
          if (res >= 500) {
              this.$q.notify({
                type: 'negative',
                message: 'Server Error'
              });
          }
        }
      }catch(error) {
        console.error(error)
        this.$q.notify({
            type: 'negative',
            message: 'Connection Error, PLease check your Network'
        });
      }
      finally {
        this.loading = false;
      }
      console.log(this.properties)
    }
  },
  computed: {
    ...mapStores(usePropertyStore)
  }
});
</script>

<style scoped>
.home-section {
  text-align: center;
}

.background-image {
  width: 100%;
  height: 400px;
  object-fit: fill;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.content-container {
  padding: 0px 0 0 0;

}

.circular-image {
  border-radius: 10%;
  width: 120px;
  height: 70px;
  object-fit: cover;
  background-image: url("assets/images/home_page_2.jpg");
}
.country{
  border-radius: 70%;
  width: 30px;
  height: 30px;
  object-fit: cover;
}

.profile-image{
  border-radius: 50%;
  width: 50px;
  height: 50px;
  object-fit: cover;
  margin-left: 50px;
}

.navbar {
  display: flex;
  justify-content: space-between;
  background-color: #faf7f7;
  padding-left: 30%;
}

.nav-item {
  margin: 0 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.nav-item:hover {
  background-color: #569ce7;
  color: #fff;
}

.nav-item i {
  margin-right: 5px;
  display: none;
}

.nav-item:hover i {
  display: inline;
}

.right-section{
  display: flex;
  width: 300px;
  padding: 10px;
  align-items: flex-end;
  background-color: #03356a;
}

.left-section{
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-button {
  background-color: #f6f8f9;
}

.action-icon {
  margin-right: 5px;
  font-size: 20px;
}

.search-bars {
  display: flex;
  justify-content: center;
  align-items: center;
  /*background-color: rgba(190, 247, 152, 0.7);*/
  background-color: #faf7f7;
  padding: 10px;
}



 .selector{
  display: flex;
  justify-content: space-around;
  margin: 1rem;
  min-width: 250px;
  min-height: 50px;
  background-color:white;
}
.search{
  display: flex;
  justify-content: space-around;
  margin: 1rem;
  min-width: 160px;
  height: 55px;
  background-color:white;
}
.search-input {
  padding: 10px;
  margin-right: 10px;
  font-size: 16px;
  width: 200px;
}

.search-button {
  padding: 10px 20px;
  background-color: #007BFF;
  color: #fff;
  border: none;
  border-radius: 5px;
  font-size: 16px;
}

.heading {
  position: absolute;
  top: 60%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-family: Arial, Helvetica, sans-serif;
  font-size: 45px;
  color: rgb(251, 247, 247);
}
.error-message {
  color: red;
  margin-left:1%;
  font-weight: bold;
  font-size: small;
}

</style>
