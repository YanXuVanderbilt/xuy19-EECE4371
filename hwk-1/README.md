# hwk-1

onCreate(), onStart(), onResume() are called before rotating 90 degrees. After rotating, onPause(), onStop(), onDestroy(), onCreate(), onStart(), onResume() are called.

The reason for this, based on Android official tutorial on lifecycles, is that device rotation is a configuration change, which requires the system to temporarily destroy the activity (and create another one shortly). That's why the system first went through onPause(), onStop(), onDestroy() to destroy the activity, then starts another activity (redrawing the screen in horizontal mode) with onCreate(), onStart(), onResume(), just like what it did before rotation.  
