<template>
  <main>
    <div class="main">

      <div class="jumbotron">
        <div style="min-height: 1rem;"></div>
        <div class="bannertext">
          <p>Find Your Future</p>
          <p >Dream Home</p>
        </div>

        <div class="container">

           <div class="row" id="row_2">
            <q-select standout v-model="Price" :options="price_sel" label="Price" class="selector"></q-select>
            <q-select standout v-model="flats" :options="flat_type_sel" label="Flat-type" class="selector"></q-select>
            <!-- <q-input standout v-model="Storey" label="Storey" class="selector" type="number" step="1" onkeydown="javascript: return ['Backspace','Delete','ArrowLeft','ArrowRight'].includes(event.code) ? true : !isNaN(Number(event.key)) && event.code!=='Space'"></q-input> -->
            <q-input standout v-model="Storey" label="Storey" class="selector"></q-input>

            <q-input standout v-model="date" label="Date" class="selector">
               <template v-slot:append>
                 <q-icon name="event" class="cursor-pointer">
                  <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                  <q-date v-model="date"></q-date>
                  </q-popup-proxy>
               </q-icon>
               </template>
          </q-input>
          <q-btn push color="white" text-color="primary" label="Search" @click="goToSearch" class="search"></q-btn>
          </div>


         </div>
         <!-- <p v-if=" Storey.toString().includes('.')|| parseInt(Storey) < 1" class="error-message" style="color: red">
           Storey cannot have decimal or less than 1
         </p> -->
         <p v-if=" Storey <0 ||Storey.toString().includes('.') " class="error-message" style="color: red">
           Storey cannot have decimal or less than 1
         </p>
         <div style="min-height: 1rem;"></div>
      </div>

      <div class="header">
        <p> </p>
        <p>  What is MyCal?</p>

      </div>
      <div class="long">
        My cal is a website that enables prospective home buyers to estimate the ent cost of a property purchase specific to the user.
        This project delivers quality of life improvments to home buyers through the use of technology to advance Singapore's Smart Nation development
        <p> </p>
      </div>

    </div>
  </main>
</template>

<script lang="ts">
import { Todo, Meta } from 'components/models';
import ExampleComponent from 'components/ExampleComponent.vue';
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'IndexPage',
  components: { ExampleComponent },
  data () {
    return {
      Storey: 0,
      Price:null,
      flats:null,
      date:null,
      flat_type_sel: ['2 Room Flexi', '3 Room','4 Room','5 Room','3 Gen Flats'],
      price_sel: ['< $300000', '$300000 - $500000', '$500001 - $700000', '$700001 - $900000','> $900000' ]
    }

  }
,methods:{
  submit(){
        const Price = this.Price;
        const flatType = this.flats;
        const storey = this.Storey;
        const selectedDate = this.date;
        // console.log('Location:', location);
        // console.log('Flat Type:', flatType);
        // console.log('Storey:', storey);
        // console.log('Date:', selectedDate);

  },
  goToSearch(){
      this.$router.push({path: "/searchrental",
      query: {
        Price: this.Price,
        flat_type: this.flats,
        storey: this.Storey,
        date: this.date,
      }})
    },

},

});
</script>

<style scoped>
.error-message {
  color: red;
  margin-left:45.5%;
  font-weight: bold;
  font-size: small;
}

main {
  display: flex;
  flex-direction: column;
  position: relative;
  flex: auto;
}

main>div {
  position: relative;
  flex: auto;
  min-height: 100%;

}
.main{
  background-color: bisque;
}
 .jumbotron{
  display: grid;
  grid-template-rows: auto min-content min-content auto;
 opacity: 0.9;
  margin: 1px auto;
  background-image: url("assets/images/home_page_2.jpg");
  background-size: 100% 100%;
  background-repeat: no-repeat;
  height: 500px;
  width: 1350px;

}
.container {
  display: flex;
  justify-content: center;
}
 .container>div{
  border-style: outset;
  border-color: black;
  border-width: small;
  border: 2px solid rgb(20, 24, 24);
  border-radius: 10px;
  background-color:  rgb(190, 207, 203);
   display: flex;
  /* justify-content: space-between;
  align-items: center; */
  flex-wrap: nowrap;
  flex-direction: row;
 }
select {
  width: 150px;
  height: 28px;
}

select option {
 width: 150px;
 height: 28px;
}
input[type=text]{
  height:28px;
  border-radius: 3px;
}
input[type=submit]{
  background-color: rgb(112, 188, 211);
  cursor: pointer;
  margin-left: 80px;
  border-radius: 13%;
  font-size:11pt;
  color:beige;
}


.bannertext{
  font-family: "Times New Roman", Times, serif;
  font-size: 50px;
  color: black;
  margin-left: 20%;
}
.header{
  text-align: center;
  font-family: cursive;
  border-bottom: 5px solid #bbb;
  margin-right: 20%;;
  margin-left:20% ;
  font-size: 20px;
}
.long{
  margin-right: 20%;;
  margin-left:20% ;
  text-align: center;
  font-family: cursive;
  border-bottom: 5px solid #bbb;
}

.q-input,
.q-select {
  margin-right: 90px; /* Adjust margin as needed */
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

  margin: 1rem;
  min-width: 130px;
  min-height: 50px;
  background-color:white;
}
</style>
