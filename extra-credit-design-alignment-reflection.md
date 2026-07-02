# Extra Credit Reflection — Design Alignment

*See `extra-credit-design-alignment.md` for submission requirements and the full assignment
description.*

**Name:** Khalid Hassan
**Date:** 07-02-2026

---

## The Audit

*List at least five concrete differences you found:*

1. Colors in Color.kt were wrong. Primary was #4F46E5, should be #6366F1. No status badge colors existed.
2. Login screen text fields had 4dp corners, should be 8dp. Focus border was gray, should be primary.
3. All buttons had 4dp corners, should be 20dp rounded.
4. Library screen status badges used wrong colors. No custom badge component.
5. Filter chips used wrong colors when active. Should be primary container. Had 4dp corners, should be 8dp.
6. Bottom nav had no active indicator pill. Colors were wrong.
7. Typography hardcoded everywhere instead of using MaterialTheme.

---

## What You Changed


### Color System
Replaced all colors in Color.kt with spec values. Added WantTo, InProgress, Finished with 
containers. Wired into Theme.kt.

### Typography

Updated Type.kt weights - Bold for H1, SemiBold for H2/H3/labels, 
Regular for body. Removed hardcoded styles.

### Buttons

Added shape = RoundedCornerShape(20.dp) to all buttons.

### Text Fields

Added shape = RoundedCornerShape(8.dp) and primary focus color.

### Other Components

Created StatusBadge. Updated chips with 8dp and primaryContainer. 
Changed cards to 12dp and 2dp. Added bottom nav indicator pill.

---

## What Was Hard

Status badge colors aren't in Material's standard colorScheme. 
Had to create custom colors and reference them directly. FilterChip doesn't auto-use 
primaryContainer I had to pass it.

---

## What You Understand Now


MaterialTheme is a CompositionLocal. Custom colors need separate definitions. 
Component shapes must be explicitly overridden from defaults.

---

## Self-Assessment

*Look at the rubric (`extra-credit-design-alignment-rubric.md`) and estimate your own score for each section. Be honest — this does not affect your grade, but it shows me whether you read the rubric carefully.*

| Section | Possible | My Estimate |
|:---|:---:|:---:|
| Color System | 13 | |
| Typography | 5 | |
| Component Styling | 15 | |
| Navigation & Cards | 5 | |
| Reflection | 12 | |
| **Total** | **50** | |

*One thing I think I did well:*

*One thing I know I left incomplete or could have done better:*
