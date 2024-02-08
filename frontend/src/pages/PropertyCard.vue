<template>
  <div class="property-card">
    <div class="card-image">
      <img :src="imageUrl" :alt="title" />
    </div>
    <div class="card-details">
      <div class="card-title">{{ title }}</div>
      <div class="card-subTitle">{{ subTitle }}</div>
      <div class="card-price">{{ price }}</div>
      <div class="card-available-date">{{ availableDate }}</div>
      <div class="card-details">{{ details }}</div>
      <div class="card-distance">{{ distance }}</div>
      <div class="card-footer">
        <div class="posted-by">Posted by: {{ postedBy }}</div>
        <router-link to="/myparticulars">
          <button class="contact-button">Contact</button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "PropertyCard",
  props: {
    title: String,
    subTitle: String,
    price: String,
    availableDate: String,
    details: String,
    distance: String,
    postedBy: String,
    imageUrl: String,
  },
};
</script>

<style scoped>
.property-card {
  width: 950px;
  height: 270px;
  margin: 20px auto;
  display: flex;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.card-image {
  width: 40%;
  display: flex;
}

.card-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: fill;
}

.card-details {
  padding: 20px;
  width: 60%; /* Adjusted width to leave some space for the image */
}

.card-title {
  font-size: 20px;
  font-weight: bold;
}

.card-subTitle {
  font-size: 15px;
  font-weight: bold;
}

.card-price {
  margin-top: 5px;
  font-size: 16px;
}

.card-available-date,
.card-details,
.card-distance {
  margin-top: 10px;
  font-size: 16px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 5px;
  font-size: 16px;
}

.posted-by {
  font-size: 16px;
  font-weight: bolder;
}

.contact-button {
  background-color: #007BFF;
  color: #fff;
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>
