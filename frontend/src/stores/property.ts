import { defineStore } from 'pinia';

import Property from 'src/interface/Property';

export const usePropertyStore = defineStore('property', {
  state: () => ({
    counter: 0,
    propertyId: null as string | null
  }),

  getters: {
  },

  actions: {
    setCurrentPropertyId(id: string) {
      this.propertyId = id;
    },

    async getCurrentPropertyId() {
      return this.propertyId;
    },

    async getPropertyBySellerId(sellerId: string): Promise<Property[]> {
      let res = await fetch(`/api/v1/accounts/${sellerId}/property`);
      return await res.json();
    },

    async createProperty(property: Property, photo: File): Promise<string| number> {
      let formData = new FormData();
      console.log(photo)
      // formData.append('photoExtension', property.photoExtension);
      formData.append("title", property.title);
      formData.append("description", property.description);
      formData.append("price", property.price.toString());
      formData.append("location", property.location);
      formData.append("flatType", property.flatType);
      formData.append("size", property.size.toString());
      formData.append("numOfBedrooms", property.numOfBedrooms.toString());
      formData.append("numOfBathrooms", property.numOfBathrooms.toString());
      formData.append("storey", property.storey.toString());
      formData.append("commenceDate", property.commenceDate);
      formData.append("photo", photo);
      //append all the attributes of property
      let res = await fetch(`/api/v1/properties`, {
        method: 'POST',
        body: formData
      });
      if (res.status === 201) {
        return await res.text();
      } else {
        return res.status;
      }
    },

    async editProperty(id: string,property: Partial<Property>, photo?: File): Promise<number> {
      let formData = new FormData();
      // formData.append('photoExtension', property.photoExtension);
      if (property.title) formData.append("title", property.title);
      if (property.description) formData.append("description", property.description);
      if (property.price) formData.append("price", property.price.toString());
      if (property.location) formData.append("location", property.location);
      if (property.flatType) formData.append("flatType", property.flatType);
      if (property.size) formData.append("size", property.size.toString());
      if (property.numOfBedrooms) formData.append("numOfBedrooms", property.numOfBedrooms.toString());
      if (property.numOfBathrooms) formData.append("numOfBathrooms", property.numOfBathrooms.toString());
      if (property.storey) formData.append("storey", property.storey.toString());
      if (property.commenceDate) formData.append("commenceDate", property.commenceDate);

      if(photo) formData.append("photo", photo);
      //append all the attributes of property
      let res = await fetch(`/api/v1/properties/${id}`, {
        method: 'PUT',
        body: formData
      });
      return res.status
    },

    async getPropertyByRecentlyAdded(): Promise<Property[]> {
      let res = await fetch(`/api/v1/properties`);
      return await res.json();
    },
    async deleteProperty(id: string): Promise<number> {
      let res = await fetch('/api/v1/properties/' + id , {method: "DELETE"});
      return res.status;

    },
    async getProperty(id: string): Promise<Property> {
      let res = await fetch('/api/v1/properties/' + id ,);
      return await res.json();

    },
    async getProperties(price?:string, flat_type?: string, story?: number, commenceDate?: string): Promise<Property[]| number> {
      let searchParams = new URLSearchParams();
      if (price) {
        searchParams.append("price", price);
      }
      if (flat_type && flat_type !== 'all') {
        searchParams.append("flatType", flat_type);
      }
      if (story){
        searchParams.append("story", story.toString());
      }
      if (commenceDate) {
        searchParams.append("commenceDate", commenceDate);
      }
      let res = await fetch('/api/v1/properties?' + searchParams.toString());
      if (res.status === 200) return await res.json()
      return res.status;
    },

    async getPropertiesByIds(ids: string[]): Promise<Property[]> {
      const promises = ids.map(async (id) => {
        const res = await fetch(`/api/v1/properties/${id}`);
        return res.json();
      });

      // Use Promise.all to wait for all promises to resolve
      return Promise.all(promises);
    },



  }
});
