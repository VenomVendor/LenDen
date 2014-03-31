/**
 * ***Copyright(c) : 2014-Present, VenomVendor.***
 * Author : VenomVendor
 * Dated : 6 Mar, 2014 1:10:48 AM
 * Project : LenDen-Android
 * Client : LenDen
 * Contact : info@VenomVendor.com
 * URL : https://www.google.co.in/search?q=VenomVendor
 * Copyright(c) : 2014-Present, VenomVendor.
 * License : This work is licensed under Attribution-NonCommercial 3.0 Unported
 * (CC BY-NC 3.0).
 * License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 * Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen.Storage;

import vee.HexWhale.LenDen.Parsers.AccessToken.GetAccessToken;
import vee.HexWhale.LenDen.Parsers.AuthCode.GetAuthCode;
import vee.HexWhale.LenDen.Parsers.Categories.GetCategory;
import vee.HexWhale.LenDen.Parsers.DetailedCategory.GetDetailedCategory;
import vee.HexWhale.LenDen.Parsers.FavCategory.GetFavCategory;
import vee.HexWhale.LenDen.Parsers.ItemCategory.GetItemCategory;
import vee.HexWhale.LenDen.Parsers.ItemStats.GetItemStats;
import vee.HexWhale.LenDen.Parsers.Messages.CreateMessage;
import vee.HexWhale.LenDen.Parsers.Messages.GetMessages;
import vee.HexWhale.LenDen.Parsers.MessagesFull.GetMessagesFull;
import vee.HexWhale.LenDen.Parsers.Profile.ChangePassword;
import vee.HexWhale.LenDen.Parsers.Profile.ForgotPassword;
import vee.HexWhale.LenDen.Parsers.Profile.GetEditProfile;
import vee.HexWhale.LenDen.Parsers.Profile.GetProfile;
import vee.HexWhale.LenDen.Parsers.ProfileItems.GetProfileItems;
import vee.HexWhale.LenDen.Parsers.SearchCategory.GetSearchCategory;

public class SettersNGetters {

    private static GetAuthCode authCode;
    private static GetAccessToken accessToken;
    private static GetAccessToken isLoggedInEmail;
    private static GetAccessToken isRegistered;
    private static GetCategory category;
    private static GetItemCategory itemCategory;
    private static GetDetailedCategory detailedCategory;
    private static GetSearchCategory searchCategory;
    private static GetFavCategory favCategory;
    private static GetMessages messages;
    private static GetMessagesFull messagesFull;
    private static GetProfile profile;
    private static GetItemStats itemStats;
    private static GetProfileItems profileItems;
    private static CreateMessage createMessage;
    private static GetEditProfile editProfile;
    private static ChangePassword changePassword;
    private static ForgotPassword forgotPassword;



    public static GetAuthCode getAuthCode() {
        return SettersNGetters.authCode;
    }

    public static void setAuthCode(GetAuthCode authCode) {
        SettersNGetters.authCode = authCode;
    }

    public static GetAccessToken getAccessToken() {
        return SettersNGetters.accessToken;
    }

    public static void setAccessToken(GetAccessToken accessToken) {
        SettersNGetters.accessToken = accessToken;
    }

    public static GetAccessToken isLoggedInViaEmail() {
        return SettersNGetters.isLoggedInEmail;
    }

    public static void setLoggedInViaEmail(GetAccessToken email) {
        SettersNGetters.isLoggedInEmail = email;
    }

    public static GetAccessToken isRegistered() {
        return SettersNGetters.isRegistered;
    }

    public static void setRegistered(GetAccessToken isRegistered) {
        SettersNGetters.isRegistered = isRegistered;
    }

    public static GetCategory getCategory() {
        return SettersNGetters.category;
    }

    public static void setCategory(GetCategory category) {
        SettersNGetters.category = category;
    }

    public static GetItemCategory getItemCategory() {
        return SettersNGetters.itemCategory;
    }

    public static void setItemCategory(GetItemCategory itemCategory) {
        SettersNGetters.itemCategory = itemCategory;
    }

    public static GetDetailedCategory getDetailedCategory() {
        return SettersNGetters.detailedCategory;
    }

    public static void setDetailedCategory(GetDetailedCategory detailedCategory) {
        SettersNGetters.detailedCategory = detailedCategory;
    }

    public static GetSearchCategory getSearchCategory() {
        return searchCategory;
    }

    public static void setSearchCategory(GetSearchCategory searchCategory) {
        SettersNGetters.searchCategory = searchCategory;
    }

    public static GetFavCategory getFavCategory() {
        return favCategory;
    }

    public static void setFavCategory(GetFavCategory favCategory) {
        SettersNGetters.favCategory = favCategory;
    }

    public static GetMessages getMessages() {
        return messages;
    }

    public static void setMessages(GetMessages messages) {
        SettersNGetters.messages = messages;
    }

    public static GetProfile getProfile() {
        return profile;
    }

    public static void setProfile(GetProfile profile) {
        SettersNGetters.profile = profile;
    }

    public static GetMessagesFull getMessagesFull() {
        return messagesFull;
    }

    public static void setMessagesFull(GetMessagesFull messagesFull) {
        SettersNGetters.messagesFull = messagesFull;
    }

    public static GetItemStats getItemStats() {
        return itemStats;
    }

    public static void setItemStats(GetItemStats itemStats) {
        SettersNGetters.itemStats = itemStats;
    }

    public static GetProfileItems getProfileItems() {
        return profileItems;
    }

    public static void setProfileItems(GetProfileItems profileItems) {
        SettersNGetters.profileItems = profileItems;
    }

    public static CreateMessage getCreateMessage() {
        return createMessage;
    }

    public static void setCreateMessage(CreateMessage createMessage) {
        SettersNGetters.createMessage = createMessage;
    }

    public static GetEditProfile getEditProfile() {
        return editProfile;
    }

    public static void setEditProfile(GetEditProfile editProfile) {
        SettersNGetters.editProfile = editProfile;
    }

    public static ChangePassword getChangePassword() {
        return changePassword;
    }

    public static void setChangePassword(ChangePassword changePassword) {
        SettersNGetters.changePassword = changePassword;
    }

    public static ForgotPassword getForgotPassword() {
        return forgotPassword;
    }

    public static void setForgotPassword(ForgotPassword forgotPassword) {
        SettersNGetters.forgotPassword = forgotPassword;
    }

}
