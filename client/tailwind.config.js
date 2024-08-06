/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    colors: {
      'black' : '#0E0805',
      'shadow' : '#25201E',
      'transparent' : 'transparent',
      'red-color' : '#E03E3E',
      'dark-red-color' : '#D74747',
      'light-red-color' : '#DD7569',
      'dark-brown-color' : '#876445',
      'medium-brown-color' : '#B8987A',
      'light-brown-color' : '#E8D4BF',
      'back-color' : '#F2EDE6',
      'white' : '#fff',
      'green' : '#38c172',
      'orange' : '#f6993f',
      'blue' : '#3490dc',
      'light-blue' : '#6cb2eb',
      'red' : '#e3342f',
      'gray' : '#6c757d',
      'pink' : '#f66d9b',
    },
    fontFamily: {
      'nunito': ['Nunito', 'sans-serif'],
      'lato': ['Lato', 'sans-serif'],
      'roboto': ['Roboto', 'sans-serif'],
      'open-sans': ['Open Sans', 'sans-serif'],
    },
    patterns: {
      opacities: {
          100: "1",
          80: ".80",
          60: ".60",
          40: ".40",
          20: ".20",
          10: ".10",
          5: ".05",
      },
      sizes: {
          1: "0.25rem",
          2: "0.5rem",
          4: "1rem",
          6: "1.5rem",
          8: "2rem",
          16: "4rem",
          20: "5rem",
          24: "6rem",
          32: "8rem",
      }
  },
    extend: {},
  },
  plugins: [
    require('tailwindcss-bg-patterns')
  ],
}

