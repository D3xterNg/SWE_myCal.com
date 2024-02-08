package sg.edu.ntu.scse.dgjmqz.myCal.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;
import sg.edu.ntu.scse.dgjmqz.myCal.ValidationGroup.PostValidation;
import sg.edu.ntu.scse.dgjmqz.myCal.ValidationGroup.PutValidation;
import sg.edu.ntu.scse.dgjmqz.myCal.entities.Property;
import sg.edu.ntu.scse.dgjmqz.myCal.model.PropertyModel;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyCtr {
    final private char[] FILE_price = (System.getProperty("user.dir")
            + "/../../frontend/public/statics/uploadedImage/property/").toCharArray();

    @Autowired
    PropertyModel propertyModel;

    /* e.g GET /api/v1/properties?title=title */

    @GetMapping
    public List<Property> getProperties(@Nullable @RequestParam(required = false) String title,

            @RequestParam(required = false) @Nullable String price,

            @RequestParam(required = false, defaultValue = "0") int story,

            @RequestParam(required = false) @Nullable String flatType,

            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") @Nullable Date commenceDate,

            @RequestParam(defaultValue = "10", required = false) int limit,

            @RequestParam(required = false, defaultValue = "0") int offset)
            throws ExecutionException, InterruptedException {
        return PropertyModel.getProperties(title, price, story, flatType,
                commenceDate, null, limit, offset);
    }

    /*
     * @GetMapping("/recent")
     * public List<Property> getPropertyByRecentlyAdded()
     * throws ExecutionException, InterruptedException {
     * return PropertyModel.getPropertyByRecentlyAdded();
     * }
     */
    /*
     * @GetMapping
     * public List<Property> getPropertiesByRecent() throws InterruptedException,
     * ExecutionException {
     * return PropertyModel.getPropertiesByRecent();
     * }
     */

    @GetMapping("/{id}")
    public Property getProperty(@PathVariable String id) throws InterruptedException, ExecutionException {
        return PropertyModel.getProperty(id, null);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postProperty(@ModelAttribute @Validated(PostValidation.class) Property property,
            @RequestPart() MultipartFile photo,
            HttpSession session) throws InterruptedException, ExecutionException, IOException {
        String sellerID = (String) session.getAttribute("accountID");
        if (sellerID == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        if (photo == null || photo.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        String[] type = photo.getContentType().split("/");
        if (!type[0].equals("image"))
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        type[1] = "." + type[1];
        File tempFile = File.createTempFile("myCalImage", type[1]);
        property.setPhotoExtension(type[1]);
        photo.transferTo(tempFile);
        property.setSellerId(sellerID);
        property.setId(null);
        property.setTime(System.currentTimeMillis());
        String newPropertyId = PropertyModel.savePropertyDetails(property);

        tempFile.renameTo(new File(new StringBuilder(FILE_price.length).append(FILE_price).append(newPropertyId)
                .append(type[1]).toString()));
        return new ResponseEntity<String>(newPropertyId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> putProperty(@PathVariable String id,
            @ModelAttribute @Validated(PutValidation.class) Property property,
            @RequestPart(required = false) @Nullable MultipartFile photo, HttpSession session)
            throws InterruptedException, ExecutionException, IOException {
        String sellerID = (String) session.getAttribute("accountID");
        if (sellerID == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        /* check if the user is the owner */
        if (PropertyModel.getPropertySeller(id).equals(sellerID)) {
            if (photo != null && !photo.isEmpty()) {
                String[] type = photo.getContentType().split("/");
                if (!type[0].equals("image"))
                    throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
                type[1] = "." + type[1];
                property.setPhotoExtension(type[1]);
                photo.transferTo(new File(new StringBuilder(FILE_price.length).append(FILE_price).append(id)
                        .append(type[1]).toString()));
            }
            PropertyModel.updatePropertyDetails(id, property);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProperty(@PathVariable String id, HttpSession session)
            throws InterruptedException, ExecutionException {
        String sellerID = (String) session.getAttribute("accountID");
        if (sellerID == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        /* check if the user is the owner */
        if (PropertyModel.getPropertySeller(id).equals(sellerID)) {
            PropertyModel.deleteProperty(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping("/properties")
    public List<Property> getPropertiesByIds(@RequestParam List<String> ids)
            throws InterruptedException, ExecutionException {
        return PropertyModel.getPropertiesByIds(ids);
    }

}
