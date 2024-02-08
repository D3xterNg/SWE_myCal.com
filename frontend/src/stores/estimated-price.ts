import { defineStore } from 'pinia';

import EstimatedPrice from '../interface/EstimatedPrice'

export const useEstimatedPriceStore = defineStore('EstimatedPrice', {
  state: () => ({

  }),

  getters: {

  },

  actions: {
    async getAllEstimatedPrice(): Promise<EstimatedPrice[]> {
      let res = await fetch(`/api/v1/flatPriceEstimate`);
      return await res.json();
    }
  }
});
