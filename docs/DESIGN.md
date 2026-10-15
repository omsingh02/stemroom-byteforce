# Design notes

## What Stemroom looks like

A rehearsal room with the lights on. Pale grey-green background, green-black text, and one mustard colour used only for the main button. Pages are plain. Lists are lists. Nothing is decorated unless it tells the reader something.

The CSS in `src/main/webapp/css/style.css` follows this file. If a rule here changes, change the CSS too.

## Why it does not look like the usual AI page

Before writing any CSS I listed the look that generated pages tend to fall into, so I could avoid it on purpose. My first draft fell into it anyway: a warm cream page, a serif headline, a terracotta accent and small uppercase monospace labels. I threw that draft out and started again.

These are the defaults I refused:

- A warm cream background with a serif headline and a terracotta or clay accent.
- A near-black background with one neon or acid-green accent.
- Cards with the same rounded corners, the same grey shadow and gradient washes.
- A small uppercase label above every heading.
- Monospace type for small data labels.
- Dots between metadata, such as "Owner · Deadline · Status", and arrows after links and buttons.
- Labels built from a bold word, a dash and a fragment.
- A hero section, big-number stat tiles, or a "Welcome back" headline.
- Blue or purple as the brand colour.

Nothing on this list appears in the app. The rest of the look follows from the subject: a music tool for people who work late, on laptops, with files and deadlines.

## Colour

| Token         | Hex       | Used for                                       |
| ------------- | --------- | ---------------------------------------------- |
| `--bg`        | `#e5e8e2` | page background                                |
| `--panel`     | `#f1f3ee` | inputs, buttons, notices                       |
| `--ink`       | `#1c2220` | text, headings, borders that need to stand out |
| `--ink-2`     | `#4f5a54` | labels, secondary text, metadata               |
| `--line`      | `#bcc5bd` | row rules and input borders                    |
| `--accent`    | `#e0a526` | fill of the primary button only                |
| `--danger`    | `#a8362a` | errors and delete actions                      |
| `--ok`        | `#2c6a43` | saved notices, approved and done statuses      |
| `--wait`      | `#7d5a00` | pending and in progress statuses               |

Rules:

- Mustard fills the primary button and nothing else. Text on it is always ink.
- Red is for errors and for things that delete or reject. It is never decoration.
- Status always shows a small square and the word, so it still reads in greyscale.

## Type

Two families, chosen to be clearly different in width:

- Headings: Franklin Gothic Medium, then Arial Narrow. A condensed grotesque, closer to a record sleeve title than to a book.
- Text: Verdana, then DejaVu Sans. Wide and plain, easy to read at small sizes on a laptop screen.

No uppercase labels, no monospace, no italic emphasis. Body text is 15px with a 1.6 line height, and lines stay near 70 characters.

Sizes: 34px page title, 22px section title, 15px text, 14px table and form labels, 13px metadata. No other sizes.

The fonts are all system fonts, so nothing is downloaded. The fallbacks still look reasonable on machines without Franklin Gothic.

## Shape and space

- Square corners everywhere. Hierarchy comes from size, weight and space, not from rounding.
- Borders are 1px or 2px. No shadows.
- Spacing is generous. The gaps used are 8, 10, 12, 16, 20, 28, 40 and 64 pixels. They were picked by eye, not by a grid overlay, so some gaps are a little uneven on purpose.
- Pages are at most 900px wide. Forms are at most 420px wide, because a form with a very wide input is hard to read.

## Parts

- Buttons: square, 2px ink border. Hover fills them with ink. The primary button is mustard with an ink border.
- Inputs: panel background, 1px line border, square corners. Focus shows a 2px ink outline.
- Tables: no zebra stripes and no vertical lines. A thin rule under each row.
- Notices: panel background with a 4px coloured left edge. Red for errors, green for saved.
- Status: a small square in the status colour, then the word.
- Lists: no bullets. A thin rule between items.
- Top bar: the brand in the heading face, links as plain text, and a 1px rule underneath. It is not a dark bar.

## Words

Sentence case everywhere, including labels and buttons. No exclamation marks, no arrows, no "Oops" and no "Let's".

Buttons say what they do: Log in, Add song, Save settings, Delete.

Errors state the fact: "Email or password is wrong."

Empty states are one plain sentence: "No songs yet."

## Layout

One column. The musician's home page is a list of their projects and songs, not a grid of tiles. The admin home page is a short definition list with two numbers and three buttons.

## Responsive

Under 640px the top bar wraps, the headings get smaller and the facts list stacks. Tables keep their columns and scroll sideways inside their own box, so the page itself never scrolls sideways.

Check the layout at 360px and at 1280px. The steps are in `TEST-CHECKLIST.md`.

## Left rough on purpose

There was no polish pass. The top bar is plain, the home page is a list, and some spacing was set by eye and not measured again. A page can be finished without being polished to a shine, and the Review 1 brief does not ask for more than that.

## How the design was checked

I wrote a short plan first: subject, palette, type, shape. Then I compared the plan with the defaults above. Where it matched a default, I changed it. That comparison was the only design review, and the palette was the part that changed most.
