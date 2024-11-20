///*
// * Copyright 2024 VAP Technologies.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
package com.charlesmuchogo.research.presentation.authentication
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.focusable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.testTag
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//@Composable
//fun WelcomeScreen() {
//    val scope = rememberCoroutineScope()
//    val carouselState =
//        rememberPagerState(initialPage = 0) { WelcomePageObject.welcomePageObjects.size }
//    val nativeComponents = koinInject<NativeComponents>()
//
//    val scrollIntervalMillis = 5000
//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(scrollIntervalMillis.toLong())
//            val nextPage =
//                (carouselState.currentPage + 1) % WelcomePageObject.welcomePageObjects.size
//            carouselState.animateScrollToPage(nextPage)
//        }
//    }
//
//    Scaffold {
//        Column(
//            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Spacer(Modifier.fillMaxSize(0.3F))
//            Column(
//                modifier = Modifier.padding(5.dp).fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                HorizontalPager(
//                    contentPadding = PaddingValues(horizontal = 25.dp),
//                    pageSpacing = 2.dp,
//                    state = carouselState,
//                ) {
//                    CarouselItem(WelcomePageObject.welcomePageObjects[carouselState.currentPage])
//                }
//
//                Text(
//                    WelcomePageObject.welcomePageObjects[carouselState.currentPage].description,
//                    modifier = Modifier.padding(vertical = 12.dp),
//                    textAlign = TextAlign.Justify,
//                    lineHeight = 24.sp,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onBackground,
//                )
//
//                LazyRow(
//                    modifier =
//                        Modifier
//                            .padding(horizontal = 8.dp)
//                            .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center,
//                ) {
//                    items(WelcomePageObject.welcomePageObjects.size) { i ->
//                        DotIndicator(
//                            selected = i == carouselState.currentPage % WelcomePageObject.welcomePageObjects.size,
//                            onClick = { scope.launch { carouselState.animateScrollToPage(i) } },
//                        )
//                        Spacer(modifier = Modifier.width(5.dp))
//                    }
//                }
//            }
//            ContinueAsComposable()
//
//            Row(
//                modifier = Modifier.fillMaxWidth().padding(top = 42.dp),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                Text(
//                    "Don't have an account? ",
//                    style = MaterialTheme.typography.bodyMedium,
//                )
//                Text(
//                    "Contact us ",
//                    modifier =
//                        Modifier.clickable {
//                            nativeComponents.openURl("https://scm.vaptechapp.com/")
//                        },
//                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun CarouselItem(welcomePageObject: WelcomePageObject) {
//    Box(
//        modifier =
//            Modifier.padding(8.dp).clip(RoundedCornerShape(8.dp))
//                .fillMaxHeight(0.3F).width(300.dp),
//        contentAlignment = Alignment.Center,
//    ) {
//        Image(
//            modifier = Modifier.fillMaxSize(),
//            painter = painterResource(welcomePageObject.resource),
//            contentDescription = null,
//            contentScale = ContentScale.Fit,
//        )
//    }
//}
//
//@Composable
//fun ContinueAsComposable() {
//    val loginTypes =
//        listOf(
//            PreferenceManager.RETAILER_LOGIN,
//            PreferenceManager.SALESMAN_LOGIN,
//            PreferenceManager.DRIVER_LOGIN,
//        )
//
//    val loginOptions =
//        listOf(
//            "Customer",
//            "Sales Rep",
//            "Driver",
//        )
//
//    val selectedOption = remember { mutableStateOf(loginOptions[0]) }
//    val navigator = LocalAppNavigator.currentOrThrow
//    val mainViewModel = koinInject<MainViewModel>()
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceEvenly,
//        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
//    ) {
//        Text(
//            "Continue as",
//            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
//            modifier = Modifier.padding(vertical = 16.dp),
//        )
//
//        Box(
//            modifier =
//                Modifier.height(60.dp).fillMaxWidth().clip(RoundedCornerShape(8.dp))
//                    .background(MaterialTheme.colorScheme.secondaryContainer),
//        ) {
//            LazyRow(
//                modifier = Modifier.fillMaxSize().padding(8.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween,
//            ) {
//                itemsIndexed(loginOptions) { index, option ->
//                    Box(
//                        modifier =
//                            Modifier.height(50.dp).width(120.dp).padding(4.dp)
//                                .clip(RoundedCornerShape(8.dp))
//                                .testTag(option)
//                                .background(
//                                    if (selectedOption.value == option) {
//                                        MaterialTheme.colorScheme.background
//                                    } else {
//                                        MaterialTheme.colorScheme.secondaryContainer
//                                    },
//                                )
//                                .clickable(
//                                    onClick = {
//                                        selectedOption.value = option
//                                        mainViewModel.saveLoginType(loginTypes[index])
//                                        navigator.push(LoginPage(loginTypes[index]))
//                                    },
//                                    indication = null,
//                                    interactionSource = remember { MutableInteractionSource() },
//                                )
//                                .focusable(false),
//                        contentAlignment = Alignment.Center,
//                    ) {
//                        Text(option, style = MaterialTheme.typography.bodyMedium)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ScmOutlinedButton(
//    modifier: Modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
//    backgroundColor: Color? = null,
//    borderColor: Color = MaterialTheme.colorScheme.primary,
//    onClick: () -> Unit,
//    content: @Composable () -> Unit,
//) {
//    val finalModifier =
//        if (backgroundColor != null) {
//            modifier.height(50.dp)
//                .border(0.5.dp, borderColor, shape = RoundedCornerShape(16.dp))
//                .clip(RoundedCornerShape(16.dp)).clickable {
//                    onClick.invoke()
//                }.background(backgroundColor)
//        } else {
//            modifier.height(50.dp)
//                .border(0.5.dp, borderColor, shape = RoundedCornerShape(16.dp))
//                .clip(RoundedCornerShape(16.dp)).clickable {
//                    onClick.invoke()
//                }
//        }
//
//    Box(
//        modifier = finalModifier,
//        contentAlignment = Alignment.Center,
//    ) {
//        content.invoke()
//    }
//}
