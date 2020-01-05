package com.example.complaintsystembeta.interfaace;

import com.example.complaintsystembeta.model.AllComplains;
import com.example.complaintsystembeta.model.Consumer;
import com.example.complaintsystembeta.model.DelayEmployees;
import com.example.complaintsystembeta.model.Department;
import com.example.complaintsystembeta.model.Designation;
import com.example.complaintsystembeta.model.Employee;
import com.example.complaintsystembeta.model.Forwards;
import com.example.complaintsystembeta.model.PostResponse;
import com.example.complaintsystembeta.model.Posts;
import com.example.complaintsystembeta.model.ReportForward;
import com.example.complaintsystembeta.model.Resolve;
import com.example.complaintsystembeta.model.SignUpData;
import com.example.complaintsystembeta.model.TestClas;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JsonApiHolder {

    @GET("check_connection")
    Call<TestClas> checkConnection();

    @GET("get_designations")
    Call<List<Designation>> getDesignations();


    @GET("get_department")
    Call<List<Department>> getDepartment();

    @GET("login")
    Call<List<SignUpData>> getPost();

    @GET("get_employee")
    Call<List<Employee>> getEmployee();

    @GET("all_complains")
    Call<List<AllComplains>> getComplains();


    @GET("get_total_delays")
    Call<List<Employee>> getTotalDelays();

    @POST("get_sorted_complains_against_date_and_status")
    @FormUrlEncoded
    Call<List<AllComplains>> getSortedComplainsAgainstDateAndStatus(@Field("date_to") String dateTo,
                                                                    @Field("date_from") String dateFrom,
                                                                    @Field("status") String status);



    @POST("get_total_coplains_by_department_sort_by_time")
    @FormUrlEncoded
    Call<List<AllComplains>> getTotalCoplainsByDepartmentSortByTime(@Field("department_name") String deptName,
                                                                    @Field("date_to") String to,
                                                                    @Field("date_from") String from);


    @POST("get_total_coplains_by_department")
    @FormUrlEncoded
    Call<List<AllComplains>> getTotalCoplainsByDepartment(@Field("department_name") String deptName);

    @POST("get_single_employee_for_profile")
    @FormUrlEncoded
    Call<Employee> getSingleEmployeeForProfile(@Field("employee_id") String employeeId);

    @POST("get_single_consumer_for_profile")
    @FormUrlEncoded
    Call<Consumer> getSingleConsumerForProfile(@Field("consumer_id") String employeeId);



    @POST("get_single_employee")
    @FormUrlEncoded
    Call<List<Employee>> getSingleEmployee(@Field("des_id") String des_id);

    @POST("update_is_seen")
    @FormUrlEncoded
    Call<TestClas> updateIsSeen(@Field("reporting_id") String reportingId);

    @POST("forget_password")
    @FormUrlEncoded
    Call<TestClas> forget(@Field("email") String email);

    @POST("update_is_delay")
    @FormUrlEncoded
    Call<TestClas> updateIsDelayed(@Field("reporting_id") String reportingId);



    @POST("update_is_acknowledged")
    @FormUrlEncoded
    Call<TestClas> updateIsAcknowledged(@Field("reporting_id") String reportingId);

    @POST("/pro")
    Call<SignUpData> testData(@Body SignUpData clas);

    @Multipart
    @POST("attachment")
    Call<TestClas> postAttachment(
            @Part MultipartBody.Part attachment,
            @Part("attachment_id") RequestBody attachment_id,
            @Part("attachment_name") RequestBody attachment_name,
            @Part("attachment_file_type") RequestBody attachment_file_type,
            @Part("complain_id") RequestBody complain_id
    );

    @Multipart
    @POST("reporting_attachment")
    Call<TestClas> reportingAttachment(
            @Part MultipartBody.Part reporting_attachment,
            @Part("reporting_attachments_id") RequestBody reporting_attachment_id,
            @Part("reporting_attachment_name") RequestBody reporting_attachment_name,
            @Part("reporting_attachment_file_type") RequestBody attachment_file_type,
            @Part("complains_reporting_id") RequestBody complains_reporting_id
    );
    @Multipart
    @POST("reporting_complains")
    Call<TestClas> postReportingComplain(
            @Part("reporting_id") String previouseReportingId,
            @Part("complain_id") RequestBody complain_id,
            @Part("complains_reporting_id") RequestBody complains_reporting_id,
            @Part("forwards_to") RequestBody forwardsTo,
            @Part("forwards_by") RequestBody forwardsBy,
            @Part("forwards_message") RequestBody forwards_message,
            @Part("suggested_date_reply") RequestBody suggested_date_reply,
            @Part("emp_name") RequestBody employee_name,
            @Part("is_reply") int isReply,
            @Part("status") RequestBody status,
            @Part("is_current") int isCurrent,
            @Part("is_seen") int isSeen,
            @Part("is_acknowledged") int isAcknowledged,
            @Part("is_public") int isPublic
    );

    @Multipart
    @POST("complains")
    Call<TestClas> postComplain(
            @Part("account_number") RequestBody account,
            @Part("complain_id") RequestBody complain_id,
            @Part("complain_status") RequestBody complain_status,
            @Part("lat") RequestBody complainant_lat,
            @Part("lng") RequestBody complainant_lng,
            @Part("complain_body") RequestBody complain_body
                                );
    @Multipart
    @POST("set_resolve_complain")
    Call<TestClas> setResolveComplain(
            @Part("resolve_id") RequestBody resolve_id,
            @Part("complain_id") RequestBody complain_id,
            @Part("resolve_by") RequestBody resolve_by,
            @Part("resolve_body") RequestBody resolve_body
                                );

    @Multipart
    @POST("verify_registeration")
    Call<TestClas> verifyRegistration(
            @Part MultipartBody.Part fileFront,
            @Part MultipartBody.Part fileBack,
            @Part("account_number") RequestBody account,
            @Part("cnic") RequestBody cnic,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("pass") RequestBody password,
            @Part("contact") RequestBody contact,
            @Part("address") RequestBody address,
            @Part("gender") RequestBody gender
    );


    @Multipart
    @POST("update_registeration")
    Call<TestClas> updateRegisteration(
            @Part MultipartBody.Part fileFront,
                                @Part MultipartBody.Part fileBack,
            @Part MultipartBody.Part fileWasaBill,
            @Part("account_number") RequestBody account,
            @Part("cnic") RequestBody cnic,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("pass") RequestBody password,
            @Part("contact") RequestBody contact,
            @Part("address") RequestBody address,
            @Part("gender") RequestBody gender
    );

    @Multipart
    @POST("registeration")
    Call<TestClas> postData(
//            @Part MultipartBody.Part fileFront,
//                                @Part MultipartBody.Part fileBack,
                                @Part MultipartBody.Part fileWasaBill,
                                @Part("account_number") RequestBody account,
                                @Part("cnic") RequestBody cnic,
                                @Part("name") RequestBody name,
                                @Part("email") RequestBody email,
                                @Part("pass") RequestBody password,
                                @Part("contact") RequestBody contact,
                                @Part("address") RequestBody address,
                                @Part("gender") RequestBody gender
    );
    @POST("single_complain_detail")
    @FormUrlEncoded
    Call<List<AllComplains>> getSingleComplainDetail(@Field("complain_id") String complainId);



    @POST("get_forward_by")
    @FormUrlEncoded
    Call<List<ReportForward>> get_forward_by(
            @Field("complain_id") String complainId
    );

    @POST("get_single_complains_forwarding")
    @FormUrlEncoded
    Call<List<ReportForward>> getSingleComplainDetailForwarding(
            @Field("complain_id") String complainId
    );

    @POST("get_complain_resolve")
    @FormUrlEncoded
    Call<Resolve> getComplainResolve(
            @Field("complain_id") String complainId
    );

    @POST("get_forward_to")
    @FormUrlEncoded
    Call<List<ReportForward>> get_forward_to(
            @Field("complain_id") String complainId
    );

    @POST("get_filter_single_complains_forwarding")
    @FormUrlEncoded
    Call<List<ReportForward>> getFilterSingleComplainDetailForwarding(
            @Field("complain_id") String complainId,
            @Field("des_id") String des_id
    );

    @POST("get_filter_single_complains_forwarding_from")
    @FormUrlEncoded
    Call<List<ReportForward>> getFilterSingleComplainDetailForwardingFrom(
            @Field("complain_id") String complainId,
            @Field("des_id") String des_id
    );
    @POST("get_filter_single_complains_forwarding_from_all_delays")
    @FormUrlEncoded
    Call<List<ReportForward>> getFilterSingleComplainDetailForwardingFromAllDelays(
            @Field("complain_id") String complainId,
            @Field("des_id") String des_id
    );
    @POST("update_status")
    @FormUrlEncoded
    Call<PostResponse> updateStatus(
            @Field("complain_id") String complainId,
            @Field("status") String status
    );



    @POST("get_forwards")
    @FormUrlEncoded
    Call<Forwards> getTotalForwards(@Field("des_id") String des_id);

    @POST("get_forwards_from")
    @FormUrlEncoded
    Call<Forwards> getTotalForwardsFrom(@Field("des_id") String des_id);

    @POST("get_total_forwards_from_with_delay")
    @FormUrlEncoded
    Call<Forwards> getTotalForwardsFromWithDelay(@Field("des_id") String des_id);

    @POST("get_forwards_complains")
    @FormUrlEncoded
    Call<List<AllComplains>> getTotalForwardsComplains(@Field("des_id") String des_id);

    @POST("get_forwards_from_complains")
    @FormUrlEncoded
    Call<List<AllComplains>> getTotalForwardsFromComplains(@Field("des_id") String des_id);

    @POST("get_forwards_from_complains_all_delays")
    @FormUrlEncoded
    Call<List<AllComplains>> getTotalForwardsFromComplainsAllDelays(@Field("des_id") String des_id);

    @POST("get_single_complains_forwarding_with_attachment")
    @FormUrlEncoded
    Call<List<ReportForward>> getSingleComplainDetailForwardingWithAttachment(@Field("complain_id") String complainId);




}
