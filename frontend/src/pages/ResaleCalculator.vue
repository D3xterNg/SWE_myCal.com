<script lang="ts">
import { mapStores } from 'pinia';
import { defineComponent } from 'vue';

import EstimatedPrice from 'src/interface/EstimatedPrice';
import Grant from 'src/interface/Grant';
import PersonalParticular from 'src/interface/PersonalParticular';
import Property from 'src/interface/Property';
import { useEstimatedPriceStore } from 'src/stores/estimated-price';
import { useGrantStore } from 'stores/grants';
import { usePersonalParticularStore } from 'stores/personal-particular';
import { usePropertyStore } from 'stores/property';
import { useSessionsStore } from 'stores/sessions';

export default defineComponent({
  name: 'ResaleCalculator',
  data () {
    return {
      properties: null as Property[][] | null,
      slide: 0,
      model: null,
      text: null,
      date: null,
      location: null,
      flat_type: null,
      location_sel: ['North',
      'South',
      'East',
      'West',
      'Central'],
      flat_type_sel: [ '2 Room Flexi',
      '3 Room',
      '4 Room',
      '5 Room',
      '3 Gen Flats',], //obtained from https://www.hdb.gov.sg/residential/buying-a-flat/finding-a-flat/types-of-flats
      personalParticulars: null as PersonalParticular | null,
      grant: null as Grant | null,
      estimatedPrice: null as EstimatedPrice | null,
      discount:0,
    }
  },
  computed: {
    ...mapStores(useSessionsStore, usePropertyStore, usePersonalParticularStore,useGrantStore,useEstimatedPriceStore),
  },

  async mounted() {
    if (this.sessionsStore.sessionAccount == null) {
      this.$router.replace('/login?from=' + encodeURIComponent('calculator'))
      return;
    }

    console.log(this.personalParticulars)
    this.personalParticulars= (await this.personalParticularStore.getPersonalParticularByUserId(this.sessionsStore.sessionAccount.accountId))
    let tempGrants= await this.GrantStore.getAllGrant()
    console.log(this.personalParticulars)
    while (tempGrants.length){

      let checkGrant=tempGrants.pop()
      console.log(checkGrant)
      if (checkGrant==null || this.personalParticulars==null) break
      if (checkGrant.familyNucleusStr!=null && checkGrant.familyNucleusStr!=this.personalParticulars.familyNucleus) continue
      if (checkGrant.ownershipHistoryStr!=null && checkGrant.ownershipHistoryStr!=this.personalParticulars.ownershipHistory) continue
      if (Number(checkGrant.income)>=Number(this.personalParticulars.income) || checkGrant.income==null){
        if (Number(checkGrant.discount)>this.discount){
          this.discount=Number(checkGrant.discount)
          console.log(this.discount)
          this.grant=checkGrant
        }
      }
    }


  },

  watch: {
    location: 'getEstimatedPrice',
    flat_type: 'getEstimatedPrice',
  },

  methods: {
    async getEstimatedPrice(){

      let tempPrice=await this.EstimatedPriceStore.getAllEstimatedPrice()
      while(tempPrice.length){
        let checkPrice=tempPrice.pop()
        if (checkPrice!=null && checkPrice.flatType==this.flat_type && checkPrice.location==this.location){
          this.estimatedPrice=checkPrice
          console.log(checkPrice)
          console.log(this.personalParticulars)
          break
        }
      }

    }
  }

});
</script>

<template>
  <main>
    <div class="row" >
      <div id="containerLeft">

        <div id="container1">
          <h2>Price Calculator</h2>
        </div>

        <div id="container2">
          <div>
            <q-select standout v-model="location" @change="getEstimatedPrice" :options="location_sel" label="Location" class="selector"></q-select>
          </div>

        </div>

        <div id="container3">
          <div>
            <q-select standout v-model="flat_type" @change="getEstimatedPrice" :options="flat_type_sel" label="Flat-type" class="selector"></q-select>
          </div>

          <!-- <div class="row">
            <div class="col" style="margin-right: 1rem;">
              <q-select standout v-model="model" :options="flat_type_sel" label="Flat-type" class="selector"></q-select>
            </div>

            <div class="col" style="margin-right: 1rem;">
              <q-input standout v-model="text" label="Storey" class="selector"></q-input>
            </div>

            <div class="col" style="">
              <q-input standout v-model="date" label="Date" class="selector">
                <template v-slot:append>
                  <q-icon name="event" class="cursor-pointer">
                    <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                      <q-date v-model="date"></q-date>
                    </q-popup-proxy>
                  </q-icon>
                </template>
              </q-input>
            </div>

          </div> -->

        </div>

        <!-- <div id="container4">
          <div>
            <q-input standout v-model="text" label="Monthly Household Income" class="selector"></q-input>
          </div>

        </div> -->

      </div>

      <!-- <div id="containerMid"></div> -->
      <div id="containerRight">

        <div id="container1">
          <h2></h2>
        </div>

        <div >
              <div class="q-gutter-md row items-start">
                <q-card
                  bordered
                  class="shadow-4"
                  style="border-radius: 10%; background-color: lightskyblue;"
                >
                  <q-card-section>
                    <div class="text-h6">Grant eligibility:</div>
                    <div class="text-subtitle2">You are{{ grant==null ? ' not' : ''}} eligible for {{ grant==null ? 'any grants' : grant.name}}.</div>
                  </q-card-section>

                  <q-separator inset></q-separator>

                  <q-card-section>
                    <div class="text-h6">Discount value:</div>
                    <div class="text-subtitle2">{{ grant==null ? 'Not applicable' : "$"+grant.discount }}</div>
                  </q-card-section>

                  <q-separator inset></q-separator>

                  <q-card-section>
                    <div class="text-h6">Estimated Undiscounted Price:</div>
                    <div class="text-subtitle2">{{ estimatedPrice==null ? "Please select a location and flat type." : "$" + estimatedPrice.estimatedPrice }}</div>
                  </q-card-section>

                  <q-separator inset></q-separator>

                  <q-card-section>
                    <div class="text-h6">Discounted Price:</div>
                    <div class="text-subtitle2">{{ personalParticulars==null? "Please fill in your personal particulars in the user tab." :
                                                  estimatedPrice==null ? "Please select a location and flat type." :
                                                  grant==null ? "$" + estimatedPrice.estimatedPrice :
                                                  "$" + (Number(estimatedPrice.estimatedPrice)-Number(grant.discount)) }}
                                                  </div>
                  </q-card-section>
                </q-card>
              </div>
        </div>
        <!-- -------------------------------------------------------------------------------------- -->
        <!-- <div id="roundContainer">
          <div id="container2">
            <p id="large">Grant eligibility</p>
            <p id="medium">You are{{ grant==null ? ' not' : ''}} eligible for {{ grant==null ? 'any grants' : grant.name}}.</p>
            <p id="medium">Discount value: ${{ grant==null ? 'Not applicable' : grant.discount }}</p>
          </div>

          <div id="container3">
            <p>Estimated Undiscounted Price:</p>
            <p>{{ estimatedPrice==null ? "Please select a location and flat type." : "$" + estimatedPrice.estimatedPrice }}</p>
          </div>

          <div id="container4">
            <p>Discounted Price:</p>
            <p>{{ personalParticulars==null? "Please fill in your personal particulars in the user tab." :
                  estimatedPrice==null ? "Please select a location and flat type." :
                  grant==null ? "$" + estimatedPrice.estimatedPrice : "$" + (Number(estimatedPrice.estimatedPrice)-Number(grant.discount)) }}</p>
          </div>
        </div> -->
      </div>
    </div>

  </main>

</template>

<style scoped>
main{
  /* margin-left: 2rem;
  margin-right: 2rem; */
  flex: auto;

  background-image: url("assets/images/calbg.jpg");
  background-size: 100% 100%;
  background-repeat: no-repeat;
  padding: 1rem;

  /* grid-template-rows: auto min-content min-content auto; */

}
main>div {
  display:  flex;
  justify-content: space-between;
}
#containerLeft{


}
#containerMid{
  min-width: 10%;

}
#containerRight{

}

#roundContainer{
  border-radius: 30%;
  background-color: aqua;
}
#container1{
  margin-left: 1rem;
  margin-right: 10rem;
  height: 8rem;
  min-height: 8rem;
}
#container2{
  margin-top: 1rem;
  height: 5rem;
  margin-left:20%;
}
#container3{
  margin-top: 1rem;
  height: 5rem;
  margin-left:20%;
/*   background-color: aqua; */
}
#container4{
  margin-top: 1rem;
  height: 5rem;
}
#large{
  font-size:2rem;
}
.selector{
  background-color:thistle;

}

</style>
