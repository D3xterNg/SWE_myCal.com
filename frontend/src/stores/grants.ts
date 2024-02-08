import { defineStore } from 'pinia';

import Grant from '../interface/Grant'

export const useGrantStore = defineStore('Grant', {
  state: () => ({

  }),

  getters: {

  },

  actions: {
    async getAllGrant(): Promise<Grant[]> {
      let res = await fetch(`/api/v1/grants`);
      return await res.json();
    }
  }
});
