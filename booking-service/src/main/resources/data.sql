-- Seed 2 tenants, a handful of shows, and enough bookings to actually paginate.

INSERT INTO show_event (tenant_id, title, venue, total_seats, price_cents, starts_at) VALUES
  (1, 'Inception',          'PVR Phoenix', 200, 45000, DATEADD('DAY', 3, CURRENT_TIMESTAMP)),
  (1, 'Interstellar',       'PVR Phoenix', 200, 45000, DATEADD('DAY', 4, CURRENT_TIMESTAMP)),
  (1, 'The Dark Knight',    'INOX Nexus',  180, 40000, DATEADD('DAY', 5, CURRENT_TIMESTAMP)),
  (2, 'Dune Part Two',      'Cinepolis',   240, 50000, DATEADD('DAY', 2, CURRENT_TIMESTAMP)),
  (2, 'Oppenheimer',        'Cinepolis',   240, 50000, DATEADD('DAY', 6, CURRENT_TIMESTAMP));

-- 60 confirmed bookings for tenant 1 / show 1 - enough to page through
INSERT INTO booking (tenant_id, show_id, user_id, seat_no, status, price_cents, created_at)
SELECT 1, 1,
       1000 + X,
       CONCAT('A', X),
       'CONFIRMED',
       45000,
       DATEADD('MINUTE', -X, CURRENT_TIMESTAMP)
FROM SYSTEM_RANGE(1, 60);

-- 30 bookings for tenant 1 / show 2 with mixed status
INSERT INTO booking (tenant_id, show_id, user_id, seat_no, status, price_cents, created_at)
SELECT 1, 2,
       2000 + X,
       CONCAT('B', X),
       CASE MOD(X, 3) WHEN 0 THEN 'CANCELLED' ELSE 'CONFIRMED' END,
       45000,
       DATEADD('MINUTE', -X * 2, CURRENT_TIMESTAMP)
FROM SYSTEM_RANGE(1, 30);

-- 15 for tenant 2 / show 4
INSERT INTO booking (tenant_id, show_id, user_id, seat_no, status, price_cents, created_at)
SELECT 2, 4,
       3000 + X,
       CONCAT('C', X),
       'CONFIRMED',
       50000,
       DATEADD('MINUTE', -X * 3, CURRENT_TIMESTAMP)
FROM SYSTEM_RANGE(1, 15);
