package com.charlesmuchogo.research.presentation.bottomBar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.presentation.clinics.ClinicsScreen
import com.charlesmuchogo.research.presentation.history.HistoryScreen
import com.charlesmuchogo.research.presentation.instructions.InstructionsScreen
import com.charlesmuchogo.research.presentation.profile.ProfileScreen

internal sealed class BottomNavigationTabs {
    internal object InstructionsTab : Tab {
        private fun readResolve(): Any = InstructionsTab

        @Composable
        override fun Content() {
            InstructionsScreen()
        }

        override val options: TabOptions
            @Composable
            get() {
                val title = "Instructions"
                val icon = painterResource(R.drawable.baseline_info_outline_24)
                return remember {
                    TabOptions(
                        index = 0u,
                        title = title,
                        icon = icon,
                    )
                }
            }
    }

    internal object HistoryTab : Tab {
        private fun readResolve(): Any = HistoryTab

        @Composable
        override fun Content() {
            HistoryScreen()
        }

        override val options: TabOptions
            @Composable
            get() {
                val title = "History"
                val icon = painterResource(R.drawable.baseline_schedule_24)
                return remember {
                    TabOptions(
                        index = 1u,
                        title = title,
                        icon = icon,
                    )
                }
            }
    }

    internal object ClinicsTab : Tab {
        private fun readResolve(): Any = ClinicsTab

        @Composable
        override fun Content() {
            ClinicsScreen()
        }

        override val options: TabOptions
            @Composable
            get() {
                val title = "Clinics"
                val icon = painterResource(R.drawable.hospital_outlined)
                return remember {
                    TabOptions(
                        index = 2u,
                        title = title,
                        icon = icon,
                    )
                }
            }
    }
    internal object ProfileTab : Tab {
        private fun readResolve(): Any = ProfileTab

        @Composable
        override fun Content() {
            ProfileScreen()
        }

        override val options: TabOptions
            @Composable
            get() {
                val title = "Profile"
                val icon = painterResource(R.drawable.outline_person_24)
                return remember {
                    TabOptions(
                        index = 3u,
                        title = title,
                        icon = icon,
                    )
                }
            }
    }
}

@Composable
fun bottomBarTabFilledIcon(item: Tab) =
    when (item.options.index) {
        (0u).toUShort() -> painterResource(R.drawable.baseline_info_24)
        (1u).toUShort() -> painterResource(R.drawable.baseline_schedule_24)
        (2u).toUShort() -> painterResource(R.drawable.hospital_filled)
        (3u).toUShort() -> painterResource(R.drawable.baseline_person_24)
        else -> painterResource(R.drawable.baseline_info_24)
    }
