INSERT INTO geocoder_problem (id, name, points, created_at, updated_at)
VALUES (1, 'COMERCIAL PRAIA E MAR LTDA', e '[ {
  "lat" : -3.6731382,
  "lng" : -38.6699059
}, {
  "lat" : -3.6730357,
  "lng" : -38.6697621
}, {
  "lat" : -3.6731817,
  "lng" : -38.6699842
}, {
  "lat" : -3.673208,
  "lng" : -38.6699312
}, {
  "lat" : -3.6731191,
  "lng" : -38.6699581
}, {
  "lat" : -3.6732181,
  "lng" : -38.6699222
}, {
  "lat" : -3.6737358,
  "lng" : -38.6697029
}, {
  "lat" : -3.6732222,
  "lng" : -38.6699006
}, {
  "lat" : -3.6730818,
  "lng" : -38.6697975
}, {
  "lat" : -3.6732287,
  "lng" : -38.6699015
}, {
  "lat" : -3.6732314,
  "lng" : -38.6700113
}, {
  "lat" : -3.673142,
  "lng" : -38.6698679
}, {
  "lat" : -3.6730763,
  "lng" : -38.6700697
}, {
  "lat" : -3.6732101,
  "lng" : -38.6699125
}, {
  "lat" : -3.6731435,
  "lng" : -38.6699616
}, {
  "lat" : -3.6732349,
  "lng" : -38.6698527
}, {
  "lat" : -3.6732957,
  "lng" : -38.669783
}, {
  "lat" : -3.6729892,
  "lng" : -38.6697663
}, {
  "lat" : -3.6737301,
  "lng" : -38.6699099
}, {
  "lat" : -3.6730899,
  "lng" : -38.6699135
}, {
  "lat" : -3.6732383,
  "lng" : -38.6699131
}, {
  "lat" : -3.6729739,
  "lng" : -38.6697346
}, {
  "lat" : -3.6733264,
  "lng" : -38.669932
}, {
  "lat" : -3.6736678,
  "lng" : -38.6702525
}, {
  "lat" : -3.6731468,
  "lng" : -38.6699141
}, {
  "lat" : -3.6727842,
  "lng" : -38.6697657
}, {
  "lat" : -3.6732081,
  "lng" : -38.6699457
}, {
  "lat" : -3.6730911,
  "lng" : -38.6698691
}, {
  "lat" : -3.6733925,
  "lng" : -38.6699214
}, {
  "lat" : -3.673201,
  "lng" : -38.6699894
}, {
  "lat" : -3.6731972,
  "lng" : -38.6699282
} ]', '2024-06-19 22:45:13.924671', '2024-06-19 22:50:22.369237') ON CONFLICT (id) DO NOTHING;

SELECT setval('geocoder_problem_pk_seq', (SELECT max(id) FROM GEOCODER_PROBLEM));