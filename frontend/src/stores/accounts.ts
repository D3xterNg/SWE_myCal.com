import { defineStore } from 'pinia';

import Account from '../interface/Account'
import PersonalParticular from "src/interface/PersonalParticular";
import BookingDetails from 'src/interface/BookingDetails';

export const useAccountStore = defineStore('Account', {
  state: () => ({

  }),

  getters: {

  },

  actions: {
    async createAccount(newAccount: Account):Promise<number> {
      let res = await fetch('/api/v1/accounts', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(newAccount)
      })
      return res.status;
    },
    async getSavedProperty(accountId: string): Promise<string[] | number> {
      let res = await fetch(`/api/v1/accounts/${accountId}/savedProperty`);
      if (res.status === 200) return await res.json();
      return res.status
    },
    async saveProperty(accountId: string, propertyId: string): Promise<number> {
      let res = await fetch(`/api/v1/accounts/${accountId}/saveProperty/${propertyId}`, {
        method: 'POST'
      });
      return res.status;
    },
    async deleteSavedProperty(accountId: string, propertyId: string): Promise<number> {
      let res = await fetch(`/api/v1/accounts/${accountId}/saveProperty/${propertyId}`, {
        method: 'DELETE'
      });
      return res.status;
    },
    async hasSavedProperty(accountId: string, propertyId: string): Promise<number> {
      let res = await fetch(`/api/v1/accounts/${accountId}/saveProperty/${propertyId}`);
      return res.status;
    },
    async updatePersonalParticular(userId: string, personalParticular: PersonalParticular): Promise<number> {
      console.log("updatePersonalParticular");
      let res = await fetch(`/api/v1/accounts/${userId}/updatePersonalParticular`, {
        method: 'POST',
        body: JSON.stringify(personalParticular),
        headers: {
          'Content-Type': 'application/json'
        }
      });
      return res.status;
    },
    async bookProperty(accountId: string, propertyId: string, bookingDetails:BookingDetails): Promise<number> {
      let res = await fetch(`/api/v1/accounts/${accountId}/bookProperty/${propertyId}`, {
        method: 'POST',
        body: JSON.stringify(bookingDetails),
        headers: {
          'Content-Type': 'application/json'
        }
      });
      return res.status;
    },
    async getBookedProperty(accountId: string): Promise<string[]> {
      let res = await fetch(`/api/v1/accounts/${accountId}/bookedProperty`);
      return await res.json();
    },
    async deleteBookedProperty(accountId: string, propertyId: string): Promise<number> {
      let res = await fetch(`/api/v1/accounts/${accountId}/bookedProperty/${propertyId}`, {
        method: 'DELETE'
      });
      return res.status;
    },

    async getPurchasedProp(accountId: string): Promise<string[]> {
      let res = await fetch(`/api/v1/accounts/${accountId}/purchasedPropID`);
      const propertiesData = await res.json();
      return propertiesData;
    },
  }
});
