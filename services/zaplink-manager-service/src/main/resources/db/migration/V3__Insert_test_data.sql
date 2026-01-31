-- Insert test data for BioPage functionality

-- Insert test bio page
INSERT INTO bio_pages (username, owner_id, theme_config, avatar_url, bio_text, created_at, updated_at) VALUES
('johndoe', 'user123', '{"primaryColor": "#3b82f6", "backgroundColor": "#f8fafc", "textColor": "#1e293b"}', 'https://picsum.photos/seed/johndoe/200/200.jpg', 'Software developer passionate about building amazing products. Check out my links below!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
RETURNING id;

-- Let's assume the returned ID is 1 (in a real scenario, you'd get this from the previous statement)

-- Insert test bio links for johndoe
INSERT INTO bio_links (page_id, title, url, type, is_active, sort_order, price, currency, created_at, updated_at) VALUES
(1, 'My Portfolio', 'https://johndoe.dev', 'LINK', true, 0, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'GitHub Profile', 'https://github.com/johndoe', 'SOCIAL', true, 1, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Premium Course - Web Development', NULL, 'PRODUCT', true, 2, 99.99, 'USD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Twitter/X', 'https://twitter.com/johndoe', 'SOCIAL', true, 3, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Contact Me', 'mailto:john@example.com', 'EMAIL', true, 4, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Book a Call', 'https://calendly.com/johndoe', 'LINK', false, 5, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert another test bio page
INSERT INTO bio_pages (username, owner_id, theme_config, avatar_url, bio_text, created_at, updated_at) VALUES
('janesmith', 'user456', '{"primaryColor": "#ec4899", "backgroundColor": "#fdf2f8", "textColor": "#831843"}', 'https://picsum.photos/seed/janesmith/200/200.jpg', 'UX/UI Designer creating beautiful digital experiences. Love coffee, design, and innovation!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
RETURNING id;

-- Insert test bio links for janesmith (assuming ID is 2)
INSERT INTO bio_links (page_id, title, url, type, is_active, sort_order, price, currency, created_at, updated_at) VALUES
(2, 'Design Portfolio', 'https://janesmith.design', 'LINK', true, 0, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Dribbble', 'https://dribbble.com/janesmith', 'SOCIAL', true, 1, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'UI Design System Template', NULL, 'PRODUCT', true, 2, 49.00, 'USD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'LinkedIn', 'https://linkedin.com/in/janesmith', 'SOCIAL', true, 3, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
