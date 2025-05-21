package sh.siava.pixelxpert.ui.activities;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;
import static sh.siava.pixelxpert.R.string.update_channel_name;
import static sh.siava.pixelxpert.ui.Constants.UPDATES_CHANNEL_ID;
import static sh.siava.pixelxpert.utils.AppUtils.isLikelyPixelBuild;
import static sh.siava.pixelxpert.utils.MiscUtils.REQUEST_EXPORT;
import static sh.siava.pixelxpert.utils.MiscUtils.REQUEST_IMPORT;
import static sh.siava.pixelxpert.utils.NavigationExtensionKt.navigateTo;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.view.MenuItem;
// import android.view.View; // Commented out or remove if not used by Compose
// import android.view.ViewGroup; // Commented out or remove if not used by Compose

import androidx.activity.ComponentActivity; // Changed import
import androidx.activity.EdgeToEdge; // For EdgeToEdge
import androidx.activity.SystemBarStyle; // For SystemBarStyle
import androidx.annotation.NonNull;
import androidx.compose.foundation.layout.Box;
import androidx.compose.foundation.layout.Row;
import androidx.compose.foundation.layout.fillMaxSize;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.AccountCircle;
import androidx.compose.material.icons.filled.Home;
import androidx.compose.material.icons.filled.Info;
import androidx.compose.material.icons.filled.Settings;
import androidx.compose.material3.Icon;
import androidx.compose.material3.NavigationBar;
import androidx.compose.material3.NavigationBarItem;
import androidx.compose.material3.NavigationRail;
import androidx.compose.material3.NavigationRailItem;
import androidx.compose.material3.Scaffold;
import androidx.compose.material3.Text;
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.getValue;
import androidx.compose.runtime.mutableStateOf;
import androidx.compose.runtime.remember;
import androidx.compose.runtime.setValue;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.platform.LocalContext;
import androidx.core.graphics.Insets; // Keep for now, might be used by EdgeToEdge or similar
import androidx.core.view.ViewCompat; // Keep for now
import androidx.core.view.WindowInsetsCompat; // Keep for now
import androidx.navigation.NavController;
// import androidx.navigation.NavGraph; // Commented out, Compose navigation is different
// import androidx.navigation.fragment.NavHostFragment; // Commented out, Compose navigation is different
// import androidx.navigation.ui.NavigationUI; // Commented out, Compose navigation is different
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import sh.siava.pixelxpert.ui.theme.PixelXpertTheme; // Import your theme

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;
import java.util.Objects;

import sh.siava.pixelxpert.BuildConfig;
import sh.siava.pixelxpert.R;
// import sh.siava.pixelxpert.databinding.SettingsActivityBinding; // ViewBinding not used
import sh.siava.pixelxpert.service.tileServices.SleepOnSurfaceTileService;
import sh.siava.pixelxpert.ui.fragments.HeaderFragment; // Will be replaced by Composable
import sh.siava.pixelxpert.ui.fragments.UpdateFragment; // Will be replaced by Composable
import sh.siava.pixelxpert.ui.preferences.preferencesearch.SearchPreferenceResult;
import sh.siava.pixelxpert.ui.preferences.preferencesearch.SearchPreferenceResultListener;
import sh.siava.pixelxpert.utils.AppUtils;
import sh.siava.pixelxpert.utils.DisplayUtils;
import sh.siava.pixelxpert.utils.ExtendedSharedPreferences;
import sh.siava.pixelxpert.utils.PrefManager;
import sh.siava.pixelxpert.utils.PreferenceHelper;


public class SettingsActivity extends ComponentActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback, SearchPreferenceResultListener {

	// private SettingsActivityBinding binding; // ViewBinding not used
	private HeaderFragment headerFragment; // Will be replaced by Composable logic
	private NavController navControllerMain; // Will be replaced by Compose NavController
	private NavController navControllerDetails; // Will be replaced by Compose NavController
	private final boolean isTabletDevice = DisplayUtils.isTablet(); // Keep this logic

	@Override
	protected void attachBaseContext(Context newBase) {
		// Logic from BaseActivity moved here
		SharedPreferences prefs = getDefaultSharedPreferences(newBase.createDeviceProtectedStorageContext());
		String localeCode = prefs.getString("appLanguage", "");
		Locale locale = !localeCode.isEmpty() ? Locale.forLanguageTag(localeCode) : Locale.getDefault();
		Resources res = newBase.getResources();
		Configuration configuration = res.getConfiguration();
		configuration.setLocale(locale);
		LocaleList localeList = new LocaleList(locale);
		LocaleList.setDefault(localeList);
		configuration.setLocales(localeList);
		super.attachBaseContext(newBase.createConfigurationContext(configuration));
		// applyOverrideConfiguration(configuration); // This was in BaseActivity, ensure it's called if needed or integrated
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// EdgeToEdge setup from BaseActivity
		EdgeToEdge.enable(
				this,
				SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
				SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
		);
		super.onCreate(savedInstanceState);
		// binding = SettingsActivityBinding.inflate(getLayoutInflater()); // ViewBinding not used
		// setContentView(binding.getRoot()); // setContentView not used with Compose

		setContent {
			PixelXpertTheme {
				SettingsActivityLayout(
						isTablet = isTabletDevice,
						// Pass necessary callbacks and state for navigation later
						// For now, just the basic structure
						navigateTo = this::handleNavigationEvent
				)
			}
		}

		createNotificationChannel();
		// setupNavigation(savedInstanceState); // Will be replaced by Compose Navigation setup

		PreferenceHelper.init(ExtendedSharedPreferences.from(getDefaultSharedPreferences(createDeviceProtectedStorageContext())));

		// Intent handling logic - will need to be adapted for Compose navigation
		if (getIntent() != null) {
			if (getIntent().getBooleanExtra("updateTapped", false)) {
				// Intent intent = getIntent();
				// Bundle bundle = new Bundle();
				// bundle.putBoolean("updateTapped", intent.getBooleanExtra("updateTapped", false));
				// bundle.putString("filePath", intent.getStringExtra("filePath"));
				// UpdateFragment updateFragment = new UpdateFragment();
				// updateFragment.setArguments(bundle);
				// navigateTo(navControllerMain, R.id.updateFragment, bundle); // Replace with Compose nav
				// For now, log or show a toast
				android.widget.Toast.makeText(this, "Update tapped intent", android.widget.Toast.LENGTH_SHORT).show();
			} else if ("true".equals(getIntent().getStringExtra("migratePrefs"))) {
				// navigateTo(navControllerMain, R.id.updateFragment, bundle); // Replace with Compose nav
				android.widget.Toast.makeText(this, "Migrate prefs intent", android.widget.Toast.LENGTH_SHORT).show();
			} else if (getIntent().getBooleanExtra("newUpdate", false)) {
				// navigateTo(navControllerMain, R.id.updateFragment); // Replace with Compose nav
				android.widget.Toast.makeText(this, "New update intent", android.widget.Toast.LENGTH_SHORT).show();
			} else if (getIntent().hasExtra(Intent.EXTRA_COMPONENT_NAME)) {
				ComponentName callerComponentName = getIntent().getParcelableExtra(Intent.EXTRA_COMPONENT_NAME, ComponentName.class);
				if(callerComponentName != null) {
					String callerClassName = callerComponentName.getClassName();
					if (SleepOnSurfaceTileService.class.getName().equals(callerClassName)) {
						// NavController navController = isTabletDevice ? navControllerDetails : navControllerMain;
						// navigateTo(navController, R.id.sleepOnFlatFragment); // Replace with Compose nav
						android.widget.Toast.makeText(this, "SleepOnSurfaceTile intent", android.widget.Toast.LENGTH_SHORT).show();
					}
				}
			}
		}

		//noinspection ConstantValue
		if (!isLikelyPixelBuild() && !BuildConfig.VERSION_NAME.contains("canary")) {
			new MaterialAlertDialogBuilder(this, R.style.MaterialComponents_MaterialAlertDialog)
					.setTitle(R.string.incompatible_alert_title)
					.setMessage(R.string.incompatible_alert_body)
					.setPositiveButton(R.string.incompatible_alert_ok_btn, (dialog, which) -> dialog.dismiss())
					.show();
		}
	}

	// Placeholder for handling navigation from Compose components
	private void handleNavigationEvent(String route) {
		// This will eventually use Compose Navigation Controller
		android.widget.Toast.makeText(this, "Navigate to: " + route, android.widget.Toast.LENGTH_SHORT).show();
		// For PreferenceFragmentCompat.OnPreferenceStartFragmentCallback, translate 'route' to fragment logic if needed temporarily
		// or ideally, navigate to a Composable destination.
	}


	// @SuppressLint({"RestrictedApi", "NonConstantResourceId"})
	// private void setupNavigation(Bundle savedInstanceState) { // Replaced by Compose Navigation
	// ... existing navigation setup code ...
	// }

	// private boolean setupOnItemSelectedListener(MenuItem item) { // Replaced by Compose Navigation
	// ... existing listener code ...
	// }

	// private void setupOnItemReselectedListener(MenuItem item) { // Replaced by Compose Navigation
	// ... existing listener code ...
	// }

	@Override
	public void onSearchResultClicked(@NonNull final SearchPreferenceResult result, NavController navController) {
		// This needs to be adapted. `navController` here is the old NavController.
		// The search result click should ideally navigate within Compose.
		// For now, we might need to pass a Composable lambda to the search component.
		headerFragment = new HeaderFragment(); // This instantiation is problematic for Compose.
		// NavController myNavController = isTabletDevice ? navControllerDetails : navController;
		// new Handler(getMainLooper()).post(() -> headerFragment.onSearchResultClicked(result, myNavController, this));
		android.widget.Toast.makeText(this, "Search result: " + result.getKey(), android.widget.Toast.LENGTH_SHORT).show();
	}

	private void createNotificationChannel() {
		NotificationManager notificationManager = getSystemService(NotificationManager.class);
		if (notificationManager != null) {
			notificationManager.createNotificationChannel(new NotificationChannel(UPDATES_CHANNEL_ID, getString(update_channel_name), IMPORTANCE_DEFAULT));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data == null) return; //user hit cancel. Nothing to do

		SharedPreferences prefs = getDefaultSharedPreferences(createDeviceProtectedStorageContext());
		switch (requestCode) {
			case REQUEST_IMPORT:
				try {
					//noinspection DataFlowIssue
					PrefManager.importPath(prefs, getContentResolver().openInputStream(data.getData()));
					AppUtils.restart("systemui");
				} catch (Exception ignored) {
				}
				break;
			case REQUEST_EXPORT:
				try {
					//noinspection DataFlowIssue
					PrefManager.exportPrefs(prefs, getContentResolver().openOutputStream(data.getData()));
				} catch (Exception ignored) {
				}
				break;
		}
	}

	@Override
	public boolean onPreferenceStartFragment(@NonNull PreferenceFragmentCompat caller, @NonNull Preference pref) {
		String key = pref.getKey();
		if (key == null) return false;

		// NavController navController = isTabletDevice ? navControllerDetails : navControllerMain; // Old nav controller

		// This logic needs to be entirely rethought for Compose.
		// We should navigate to Composable destinations instead of Fragments.
		// For now, let's just log the attempt.
		android.widget.Toast.makeText(this, "Attempt to start fragment for pref: " + key, android.widget.Toast.LENGTH_LONG).show();
		handleNavigationEvent("preference_screen/" + key);


		// Temporarily, we can return false to prevent fragment transactions,
		// or if some fragments are still used, specific logic might be needed.
		// For a full Compose migration, this callback should ideally not be used.
		return true; // Returning true to allow old fragment logic if any part still relies on it, though this is not ideal.

		// return switch (key) {
		// // ... cases from original code ...
		// default -> false;
		// };
	}

	@Override
	protected void onNewIntent(@NonNull Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		// Handle new intent, potentially for navigation
		// e.g., if (intent.hasExtra("navigateTo")) { handleNavigationEvent(intent.getStringExtra("navigateTo")); }
	}
}

@Composable
fun SettingsActivityLayout(isTablet: Boolean, navigateTo: (String) -> Unit) {
	var selectedItem by remember { mutableStateOf(0) }
	val items = listOf("Home", "Updates", "Hooks", "Settings")
	val icons = listOf(Icons.Filled.Home, Icons.Filled.Info, Icons.Filled.AccountCircle, Icons.Filled.Settings)

	Scaffold(
		bottomBar = {
			if (!isTablet) {
				NavigationBar {
					items.forEachIndexed { index, item ->
						NavigationBarItem(
							icon = { Icon(icons[index], contentDescription = item) },
							label = { Text(item) },
							selected = selectedItem == index,
							onClick = {
								selectedItem = index
								navigateTo(item.lowercase(Locale.getDefault())) // Example navigation route
							}
						)
					}
				}
			}
		}
	) { innerPadding -> // innerPadding is provided by Scaffold
		Row(Modifier.fillMaxSize()) {
			if (isTablet) {
				NavigationRail {
					items.forEachIndexed { index, item ->
						NavigationRailItem(
							icon = { Icon(icons[index], contentDescription = item) },
							label = { Text(item) },
							selected = selectedItem == index,
							onClick = {
								selectedItem = index
								navigateTo(item.lowercase(Locale.getDefault())) // Example navigation route
							}
						)
					}
				}
			}
			// Main content area - Placeholder for now
			// This is where the content of the selected screen (equivalent to fragments) will go.
			// For example, based on selectedItem, display different Composables.
			Box(modifier = Modifier.weight(1f).padding(innerPadding)) { // Use innerPadding
				Text("Content for ${items[selectedItem]}")
				// When ready, this would be:
				// when (selectedItem) {
				// 0 -> HomeScreen()
				// 1 -> UpdatesScreen()
				// 2 -> HooksScreen()
				// 3 -> MainSettingsScreen() // This would then have sub-screens for preferences
				// }
			}

			if (isTablet) {
				// Detail content area for tablets - Placeholder for now
				Box(modifier = Modifier.weight(1.5f).padding(innerPadding)) { // Use innerPadding
					Text("Detail Pane (Tablet)")
				}
			}
		}
	}
}
