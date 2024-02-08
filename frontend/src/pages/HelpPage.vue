<template>
  <div class="q-px-xl q-py-lg" absolute-centre>
    <div id="leftDiv">
      <p class="text-h5">
        FAQs
        <q-separator inset />
      </p>
      <q-list class="rounded-borders">
        <q-expansion-item
          expand-separator
          icon="perm_identity"
          label="What is MyCal?"
        >
          <q-card>
            <q-card-section>
              My cal is a website that enables prospective home buyers to estimate the ent
              cost of a property purchase specific to the user. This project delivers quality
              of life improvments to home buyers through the use of technology to advance
              Singapore's Smart Nation development
            </q-card-section>
          </q-card>
        </q-expansion-item>

        <q-expansion-item
          expand-separator
          icon="calculate"
          label="How do I use the Rental Calculator?"
        >
          <q-card>
            <q-card-section>
              If you're having trouble with using the Rental Calculator, it might be because you
              haven't updated your <router-link to="/viewparticulars">Personal Particulars</router-link>.
              Other than that, all you have to do now is input
              the Location and Flat-Type, and the calculator would output the presented Grants and Subsidies.
            </q-card-section>
          </q-card>
        </q-expansion-item>

        <q-expansion-item
          expand-separator
          icon="chat"
          label="How do I contact the agent?"
        >
          <q-card>
            <q-card-section>
              To contact an agent, you would have to click the Book Button which would prompt an email to the Seller.
            </q-card-section>
          </q-card>
        </q-expansion-item>

        <q-expansion-item
          icon="payment"
          label="What are the payment methods?"
        >
          <q-card>
            <q-card-section>
              The payments methods can be discussed with the Seller through Email, however most Sellers would
              prefer online transactions
            </q-card-section>
          </q-card>
        </q-expansion-item>

        <q-expansion-item
          expand-separator
          icon="cancel"
          label="How can I cancel my bookings?"
        >
          <q-card>
            <q-card-section>
              Cancellation of Booking can be done by simply contacting the Seller and discussing the refund methods.
              Note that you cannot return properties that have already been sold to you.
            </q-card-section>
          </q-card>
        </q-expansion-item>
      </q-list>
    </div>
    <div id="rightDiv">
      <q-card bordered inline class="q-pa-sm">
        <q-card-title class="text-body1 text-weight-bold">Send us a Message</q-card-title>
          <p class = "text-body1">Contact our agents about any exquiries, and we'll reply as soon as possible.</p>

        <q-card-separator />
        <q-card-actions vertical>
          <q-btn color="secondary" icon-right="chat" label="Whatsapp" class="q-my-sm" @click="openWhatsapp"/>
          <q-btn color="orange" icon-right="email" label="Email" class="q-mb-md" @click="openEmailClient"/>
        </q-card-actions>

        <q-card-title class="text-body1 text-weight-bold">Call us</q-card-title>
          <p class = "text-body1">For any emergency, you can call us 24/7 on a local or international phone number.</p>
          <q-card-separator />
          <div class = "text-body1 q-mb-sm">
            <span class="text-weight-bold">International:</span>
             +01-7819 3729
             <div></div>
             <span class="text-weight-bold">Local:</span>
             +65-6553 3756

          </div>

      </q-card>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { mapStores } from 'pinia'

import { useSessionsStore } from 'stores/sessions';
import { usePropertyStore } from 'stores/property'
import Account from 'src/interface/Account';
import Property from 'src/interface/Property'
import { useAccountStore } from 'stores/accounts'
import PropertyCard from 'components/PropertyCard.vue'
import PropertyList from "src/components/PropertyList.vue";

export default defineComponent({
  name: "HelpPage",
  methods: {
    openWhatsapp() {
      const url = 'https://www.whatsapp.com/';
      window.open(url, '_blank');
    },

    openEmailClient() {
      if (this.sessionsStore?.sessionAccount) {
        const email = 'mailto:mycal_web@hotmail.com'; // This shall be the temporary email for the website
        const subject = '?subject=Enquiries%20on%20MyCal%20Website';

        window.location.href = email + subject;
      }
        else {
          this.$router.replace({path: '/login', query: { from: '/helppg' }})
          return;
        }

    },

    callPhoneNumber() {
      // Specify the phone number you want to link
      const phoneNumber = 'tel:+1234567890';

      // Open the phone dialer with the specified phone number
      window.location.href = phoneNumber;
    }
  },
  computed: {
    ...mapStores(useAccountStore, useSessionsStore)
  }
});
</script>

<style scoped>
#leftDiv {
  float: left;
  width: 60%;
  height: 100%;
}
#rightDiv {
  float: right;
  width: 35%;
  height: 100%;
}

</style>
