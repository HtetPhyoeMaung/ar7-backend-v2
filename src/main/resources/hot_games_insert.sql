-- Hot Games Initial Data
-- This SQL inserts the complete hot games JSON data
-- The JSON is stored as TEXT in the game_name column with id=1

INSERT INTO hot_games (id, game_name)
SELECT 1, $HOTGAMESJSON$$
-- Full JSON data will be inserted here
$$HOTGAMESJSON$$
FROM (SELECT 1) AS tmp WHERE NOT EXISTS (SELECT 1 FROM hot_games WHERE id = 1);
