$(function() {
	
	$("#slide_menu ul li").on('mouseover', 'a', function() {
		let parentId = $(this).attr('data-parent-category-id');
		$("#childCategory ul").remove();
		let childMenuItems = ``;
		
		function createSubcategoryMenu(subcategories) {
			subcategories.forEach(function(category) {
				if(category.childCategories && category.childCategories.length > 0) {
					childMenuItems += `<li class="p-l-10 p-r-10" style="font-weight: bold;">
						<a href="/category/parent/${category.categoryId}/product/sort" style="color: #fff;" class="m-l-10"> ${category.categoryName} </a>`;
				}
				else {
					childMenuItems += `<li class="p-l-10 p-r-10" style="font-weight: bold;">
						<a href="/category/${category.parentId}/${category.categoryId}/product/sort" style="color: #fff; margin-left: 5px;"> ${category.categoryName} </a>`;	
				}
				if(category.childCategories && category.childCategories.length > 0) {
					childMenuItems += `<ul>`;
					createSubcategoryMenu(category.childCategories);
					childMenuItems += "</ul>";
				}
				
				childMenuItems += "</li>";
			});
		}
		
		//searchfor each object in the category listing.
		categoriesData.forEach(function(category) {
			if(parentId == category.categoryId) {
				if(category.childCategories && category.childCategories.length > 0) {
					childMenuItems += `<ul class="row m-t-5">`;
					createSubcategoryMenu(category.childCategories);
					childMenuItems += `</ul>`;
				}
				
			}
		});
		
		/*for(let i = 0; i < rawChildCategories.length; i++) {
			for(childCategory of rawChildCategories[i]) {
				if(childCategory.parentId == parentId) {
					childMenuItems += ` <div class="col-md-3">
						<a style="color: #fff;" href="" class="m-l-10">` + childCategory.categoryName +`</a>
							<ul class="m-l-15 m-b-15 fs-12">
								<li> <a href="">Category</a> </li>
								<li> <a href="">Category</a> </li>
								<li> <a href="">Category</a> </li>
								<li> <a href="">Category</a> </li>
							</ul>
						</div>`;
				}
			}
		}*/
		
		
		$("#childCategory").append(childMenuItems);
		$("#slide_menu, #childCategory").on('mouseleave', function() {
			$("#childCategory ul").hide();
		});
	});
	
	

	
});